import React from "react";
import ChooseApp from "../pages/ChooseApp";
import ValidationRequest from "./ValidationRequest";
import RequestAccess from "../pages/RequestAccess";

class GetUserTask extends React.Component{


    state = {
        found: false,
        task_name: null,
        hasRequiredAccess: false,
        valChoice: null,
        appChoice: null,
        admin: "",
        userPending: "",
        appPending: ""
    }

    componentDidMount() {
        this.getUserTask(this.props.user) //get the tasks
        this.getLastAdmin() //get the last admin
    }

    //update the tasks
    componentDidUpdate(prevProps, prevState, snapshot) {
        this.getUserTask(this.props.user)
    }

    getLastUserChoice = () => {
        fetch("http://localhost:8081/api/getLastUserApp", {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then(response => {
            return response.json();
        }).then(data => {
            this.setState({userPending: data.user, appPending: data.value})
        }).catch(error => {
            console.log(error);
        });
    }

    getUserTask = (user) => {
        fetch("http://localhost:8081/api/task/" + `${user}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then(response => {
                return response.json();
        }).then(data => {
            if(data !== null)
                this.setState({found: true, task_name: data.name_});
        }).catch(error => {
            this.setState({task_name: null})
            console.log(error);
        });

    }

    setValChoice = (val) => {
        this.setState({
            valChoice: val
        })
    }

    setChoice = (choice) => {
        this.setState({
            appChoice: choice
        })
        this.state.appPending = choice;
    }

    setUserPending = (user) => {
        this.state.userPending = user;
    }

    hasRequiredAccess = (access) => {
        this.setState({ hasRequiredAccess: access})
        fetch("http://localhost:8081/api/userRequiredAccess/" + `${this.props.user}`, {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
    }

    //it manages admin tasks
    handleConfirmRequest = () => {
        console.log(this.state.admin + " " + this.state.userPending)
        fetch("http://localhost:8081/api/request/" + `${this.state.admin}` + "&" + `${this.state.userPending}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
    }
/*
    //last user task
    handleEndUser = () => {
        fetch("http://localhost:8081/api/request/onlyUser/" + `${this.state.userPending}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
    }
*/
    addAppValidatedByAdmin = () => {
        fetch("http://localhost:8081/api/addAppValidated/" + `${this.state.userPending}` + "&" + `${this.state.appPending}` + "&" + `${this.state.admin}`, {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
    }


    handleConfirmAppConnected = () => {
        //this.handleEndUser()
        this.addAppValidatedByAdmin()
    }

    setAdmin = (user) =>{
        //this.state.admin = user;
        fetch("http://localhost:8081/api/insertAdmin/" + `${user}`, {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
    }

    getLastAdmin = () => {
        fetch("http://localhost:8081/api/getLastAdmin", {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then(response => {
            return response.json();
        }).then(data => {
            console.log(data.username)
            this.setState({ admin: data.username})
        }).catch(error => {
            console.log(error);
        });
    }

    renderTask = () => {
        if(this.state.task_name === "choose the application"){
            this.setUserPending(this.props.user)
            return(
                <div>
                    <ChooseApp
                        user={this.props.user}
                        isAdmin={this.props.isAdmin}
                        onSignOut={this.props.onSignOut}
                        choice={this.setChoice.bind(this)}
                    />
                </div>
            )
        } else if (this.state.task_name === "app connected"){
            this.getLastUserChoice()
            return(
                <div>
                    <h4> You have access to the following application: {this.state.appPending} </h4>
                    <input type="submit" value="Confirm" onClick={this.handleConfirmAppConnected.bind(this)}/>
                </div>
            )
        } else if (this.state.task_name === "validation request"){
            this.setAdmin(this.props.user)
            this.getLastUserChoice()
            return(
                <div>
                    <p>{this.state.userPending} is requiring access to the following app: <strong>{this.state.appPending}</strong></p>
                    <p>Do you want to validate the request?</p>
                    <ValidationRequest
                        validationChoice={this.setValChoice.bind(this)}
                        user={this.state.admin}
                        onSignOut={this.props.onSignOut}
                    />
                </div>
            )
        } else if (this.state.task_name === "request validated"){
            this.getLastUserChoice()
            return(
                <div>
                    <p>The request has been validated.</p>
                    <input type="submit" value="Confirm" onClick={this.handleConfirmRequest.bind(this)}/>
                </div>
            )
        } else if (this.state.task_name === "request not validated"){
            this.getLastUserChoice()
            return (
                <div>
                    <p>The request has not been validated.</p>
                    <p>{this.state.userPending} will receive an email explaining the reason he didn't get the permissions.</p>
                    <input type="submit" value="Confirm" onClick={this.handleConfirmRequest.bind(this)}/>
                </div>
            )
        }
    }

    handleSignOut = (e) => {
        e.preventDefault()
        this.props.onSignOut(e)
    }

    render() {
        return(
            <div>
                {
                    (this.state.found === true) ?
                        <div>
                            <h3>{this.state.task_name}</h3>
                            {this.renderTask()}
                        </div>
                        :
                        <div>
                        </div>
                }
                <div>
                    {
                        (this.state.task_name === "request access" || this.state.task_name === null) ?
                        <div>
                            <RequestAccess
                            user={this.props.user}
                            isAdmin={this.props.isAdmin}
                            onSignOut={this.props.onSignOut}
                            hasRequiredAccess={this.hasRequiredAccess.bind(this)}
                            />
                        </div>:
                            <div>

                            </div>
                }
                    <p>
                        <input type="submit" value="Sign Out" onClick={this.handleSignOut.bind(this)}/>
                    </p>
                </div>
            </div>
    )
    }
}

export default GetUserTask;