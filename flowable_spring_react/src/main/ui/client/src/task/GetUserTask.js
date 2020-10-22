import React from "react";
import ChooseApp from "../pages/ChooseApp";
import RequestAccess from "../pages/RequestAccess";

class GetUserTask extends React.Component{

    constructor(props) {
        super(props);
        this.getAllRequest()
    }

    state = {
        found: false,
        task: [],
        hasRequiredAccess: false,
        valChoice: [],
        appChoice: null,
        userPending: "",
        appPending: "",
        n_request: 0,
        request_user: [],
        selectedOption: [],
        changeStateRadioButton: []
    }

    componentDidMount() {
        this.n_request()
        this.getUserTask(this.props.user)
        this.getLastAdmin() //get the last admin
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
            if(data !== null) {
                for (let key in data) {
                    if (Object.prototype.hasOwnProperty.call(data, key)) {
                        let val = data[key];
                        this.state.task.push({id: val.id_, name: val.name_, assignee: val.assignee_})
                    }
                    this.setState({ found: true})
                }
            }
        }).catch(error => {
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
        const users = [];
        for(let i=0; i<this.state.request_user.length; i++) {
            users.push(this.state.request_user[i].user);
        }

        //for(let i=0; i<this.state.request_user.length; i++) {
        fetch("http://localhost:8081/api/request/" + `${this.state.admin}` + "&" + `${users}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
        alert("Task executed.")
        window.location.reload()
        //}
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
        console.log(this.props.user)
        let app;
        for(let i=0; i<this.state.request_user.length; i++){
            if(this.state.request_user[i].user === this.props.user){
                app = this.state.request_user[i].app;
            }
        }
        fetch("http://localhost:8081/api/addAppValidated/" + `${this.props.user}` + "&" + `${app}` + "&" + `${this.state.admin}`, {
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

    deleteRowTable = () => {
        let app;
        for(let i=0; i<this.state.request_user.length; i++){
            if(this.state.request_user[i].user === this.props.user){
                app = this.state.request_user[i].app;
            }
        }
        for(let i=0; i<this.state.request_user.length; i++) {
            fetch("http://localhost:8081/api/deleteUserApp/" + `${this.props.user}` + "&" + `${app}`, {
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
    }

    handleConfirmAppConnected = () => {
        //this.handleEndUser()
        this.addAppValidatedByAdmin()
        this.deleteRowTable()
        alert("Task executed.")
        window.location.reload()
    }

    //get all request from DB: ACT_ID_USER_APP
    getAllRequest = () => {
        fetch("http://localhost:8081/api/getAllRequest", {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then(response => {
            return response.json();
        }).then(data => {
            for (let key in data) {
                if (Object.prototype.hasOwnProperty.call(data, key)) {
                    let val = data[key];
                    this.state.request_user.push({user: val.user, app: val.value})
                }
            }
        }).catch(error => {
            console.log(error);
        });
    }

    n_request = () => {
        fetch("http://localhost:8081/api/countRequest", {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then(response => {
            return response.json();
        }).then(data => {
            //console.log(data)
            this.setState({ n_request: data})
        }).catch(error => {
            console.log(error);
        });
    }

    //get the task with the id, passed by api call (fetch) -> DOESN'T WORK
    getTaskByID = (id) => {
        fetch("http://localhost:8081/api/task/getTaskById/" + `${id}`, {
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
            //console.log(data.username)
            this.setState({ admin: data.username})
        }).catch(error => {
            console.log(error);
        });
    }

    handleOptionChange = (e) => {
        this.state.changeStateRadioButton[e.target.accessKey] = e.target.value
        this.state.selectedOption.push({user: e.target.id, value: e.target.value, app: e.target.name})
        this.forceUpdate()
    }

    handleFormSubmit = (e) => {
        e.preventDefault()
        console.log(this.state.changeStateRadioButton)
        let c = [];
        for(let i=0; i<this.state.changeStateRadioButton.length; i++){
            if(this.state.request_user[i].user === this.state.selectedOption[i].user){
                c[i] = this.state.changeStateRadioButton[i]
            }
        }
        console.log(c)
        //this.props.validationChoice(c)
        fetch("http://localhost:8081/api/choiceValidationForm/" + `${c}` + "&" + `${this.props.user}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
        alert("Task executed.")
        window.location.reload()
    }

    renderTask = () => {
        console.log(this.state.task)
        const vectorRender = []
        if(this.props.isAdmin === false) {
            for (let i = 0; i < this.state.task.length; i++) {
                if (this.state.task[i].name === "choose the application") {
                    this.setUserPending(this.props.user)
                    vectorRender.push(
                        <div className="chooseApplication">
                            <h3>{this.state.task[i].name}</h3>
                            <ChooseApp
                                user={this.props.user}
                                isAdmin={this.props.isAdmin}
                                onSignOut={this.props.onSignOut}
                                choice={this.setChoice.bind(this)}
                            />
                        </div>
                    )
                }
                if (this.state.task[i].name === "app connected") {
                    let app = null;
                    for(let i=0; i<this.state.request_user.length; i++){
                        if(this.state.request_user[i].user === this.props.user){
                            app = this.state.request_user[i].app;
                        }
                    }
                    vectorRender.push(
                        <div>
                            <h3>{this.state.task[i].name}</h3>
                            <h4> You have access to the following application: {app} </h4>
                            <input type="submit" value="Confirm" id="appConnectedBtn" onClick={this.handleConfirmAppConnected.bind(this)}/>
                        </div>
                    )
                }
            }
        }else {
            console.log(this.state.n_request)
            console.log(this.state.task)
            if (this.state.n_request > 0) {
                for (let i = 0; i < this.state.task.length; i++) {
                    console.log(i + " " + this.state.task[i].name + " " +  this.state.request_user[i].user)
                    if (this.state.task[i].name === "validation request") {
                        vectorRender.push(
                            <div className="validationRequest">
                                <h3>{this.state.task[i].name}</h3>
                                <div className="validationRequestTask">
                                    <p><strong>{this.state.request_user[i].user}</strong> is requiring access to the following app: <strong>{this.state.request_user[i].app}</strong></p>
                                    <p>Do you validate the request?</p>
                                    <form id="formRadioButtons">
                                        <div id="valChoices">
                                            <input className="inputYes" accessKey={i} name={this.state.request_user[i].app} type="radio" id={this.state.request_user[i].user} value="Yes" checked={this.state.changeStateRadioButton[i] === 'Yes'} onChange={this.handleOptionChange.bind(this)}/>
                                            <label id="forYes" htmlFor="yes">Yes</label>
                                            <br/>
                                            <input className="inputNo" accessKey={i} name={this.state.request_user[i].app} type="radio" id={this.state.request_user[i].user} value="No" checked={this.state.changeStateRadioButton[i] === 'No'} onChange={this.handleOptionChange.bind(this)}/>
                                            <label id="forNo" htmlFor="no">No</label>
                                        </div><br/>
                                    </form>
                                </div>

                            </div>)
                        /*
                        {this.state.request_user.map((user, i) =>
                                    <div className="validationRequestTask">
                                        <p><strong>{user.user}</strong> is requiring access to the following app: <strong>{user.app}</strong></p>
                                        <p>Do you validate the request?</p>
                                        <form id="formRadioButtons">
                                            <div id="valChoices">
                                                <input className="inputYes" accessKey={i} name={user.app} type="radio" id={user.user} value="Yes" checked={this.state.changeStateRadioButton[i] === 'Yes'} onChange={this.handleOptionChange.bind(this)}/>
                                                <label id="forYes" htmlFor="yes">Yes</label>
                                                <br/>
                                                <input className="inputNo" accessKey={i} name={user.app} type="radio" id={user.user} value="No" checked={this.state.changeStateRadioButton[i] === 'No'} onChange={this.handleOptionChange.bind(this)}/>
                                                <label id="forNo" htmlFor="no">No</label>
                                            </div><br/>
                                        </form>
                                    </div>
                                )}
                         */
                    } else {
                        if (this.state.task[i].name === "request validated") {
                            vectorRender.push(
                                <div className="requestValidatedDiv">
                                    <h3>{this.state.task[i].name}</h3>
                                    <div className="requestValidatedTask">
                                        <p><strong>{this.state.request_user[i].user}</strong>'s request has been validated</p>
                                    </div>

                                </div>
                            )
                            /* WITH THE MAP
                                   {this.state.request_user.map((user) =>
                                        <div>
                                            <p> <strong>{user.user}</strong>'s request has been validated</p>
                                        </div>
                                    )}
                             */
                        } else if (this.state.task[i].name === "request not validated") {
                            vectorRender.push(
                                <div className="requestNotValidatedDiv">
                                    <h3>{this.state.task[i].name}</h3>
                                    <div className="requestNotValidatedTask">
                                    <p>The request has not been validated.</p>
                                    <p><strong>{this.state.request_user[i].user}</strong> will receive an email explaining the reason he
                                        didn't get the permissions.</p>
                                    </div>
                                </div>
                            )
                        }
                    }
                }
            }
            let isARequest = false;
            for(let i=0; i<this.state.task.length; i++){
                if(this.state.task[i].name === "request validated" || this.state.task[i].name === "request not validated")
                    isARequest = true;
                if(this.state.task[i].name === "validation request")
                    isARequest = false;
            }
            if(isARequest === true) {
                vectorRender.push(<div><input type="submit" value="Confirm" id="requestAccessBtn2" onClick={this.handleConfirmRequest.bind(this)}/></div>)
            } else {
                vectorRender.push(<div><input type="submit" id="confirmValidationBtn" value="Confirm" onClick={this.handleFormSubmit.bind(this)}/></div>)
            }
        }
        return vectorRender;
    }

    handleSignOut = (e) => {
        e.preventDefault()
        this.props.onSignOut(e)
        window.location.reload()
    }

    render() {
        console.log(this.state.found + " " + this.state.task.length + " " + this.props.isAdmin)
        return(
            <div>
                {
                    (this.state.found === true && this.state.task.length > 0) ?
                        <div>
                            {this.renderTask()}
                        </div>
                        :
                        <div>
                            <RequestAccess
                                user={this.props.user}
                                isAdmin={this.props.isAdmin}
                                onSignOut={this.handleSignOut.bind(this)}
                                hasRequiredAccess={this.hasRequiredAccess.bind(this)}
                            />
                        </div>
                }
                <div>
                    {
                        (this.state.task === null) ?
                        <div>
                            <RequestAccess
                                user={this.props.user}
                                isAdmin={this.props.isAdmin}
                                onSignOut={this.handleSignOut.bind(this)}
                                hasRequiredAccess={this.hasRequiredAccess.bind(this)}
                            />
                        </div>:
                            <div>

                            </div>
                }
                    <p>
                        <input type="submit" id="signOutBtn" value="Sign Out" onClick={this.handleSignOut.bind(this)}/>
                    </p>
                </div>
            </div>
        )
    }
}

export default GetUserTask;