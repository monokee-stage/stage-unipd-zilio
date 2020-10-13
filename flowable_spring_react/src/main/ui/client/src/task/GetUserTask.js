import React from "react";
import Axios from "axios";
import ChooseApp from "../pages/ChooseApp";
import AppConfirmed from "../pages/AppConfirmed";
import ValidationRequest from "./ValidationRequest";
import RequestAccess from "../pages/RequestAccess";
import Registration from "../pages/Registration";

class GetUserTask extends React.Component{

    state = {
        found: false,
        task_name: null,
        hasRequiredAccess: false,
        valChoice: null,
        appChoice: null,
        userPending: null
    }

    componentDidMount() {
        this.getUserTask(this.props.user)
    }

    getLastUserChoice = (id) => {
        fetch("http://localhost:8081/api/getLastAppUserNoProcess/" + `${id}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then(response => {
            return response.json();
        }).then(data => {
            this.setState({appChoice: data.value})
        }).catch(error => {
            console.log(error);
        });
        return this.state.appChoice
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
            //console.log(data.name_)
            this.setState({found: true, task_name: data.name_});
        }).catch(error => {
            console.log(error);
            this.setState({task_name: null})
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
    }

    setUserPending = (user) => {
        this.setState({
            userPending: user
        })
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

    handleConfirmRequest = () => {
        fetch("http://localhost:8081/api/request/" + `${this.props.user}`, {
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

    handleConfirmAppConnected = () => {
        this.handleConfirmRequest()
        return (
            <h3>Workflow ended!</h3>
        )
    }

    renderTask = () => {
        if(this.state.task_name === "choose the application"){
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
            return(
                <div>
                    <h4> You have access to the following application: {this.getLastUserChoice(this.props.user)} </h4>
                    <input type="submit" value="Confirm" onClick={this.handleConfirmAppConnected.bind(this)}/>
                </div>
            )

        } else if (this.state.task_name === "validation request"){
            return(
                <div>
                    <ValidationRequest
                        validationChoice={this.setValChoice.bind(this)}
                        onSignOut={this.props.onSignOut}
                    />
                </div>
            )
        } else if (this.state.task_name === "request validated"){
            return(
                <div>
                    <p>Your request has been validated.</p>
                    <input type="submit" value="Confirm" onClick={this.handleConfirmRequest.bind(this)}/>
                </div>
            )
        } else if (this.state.task_name === "request not validated"){
            return (
                <div>
                    <p>Your request has not been validated.</p>
                    <p>You will receive an email explaining the reason you didn't get the permissions.</p>
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