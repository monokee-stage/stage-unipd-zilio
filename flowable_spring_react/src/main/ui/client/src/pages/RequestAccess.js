import React, {Component} from "react";

class RequestAccess extends Component{

    handleSubmit = () => {
        console.log(this.props.user + " required access")
        this.props.hasRequiredAccess(true);
        alert("Task executed")
        window.location.reload()
    }

    render() {
        return (
            <div className="requestAccess">
                <input type="submit" id="requestAccessBtn" value="Request access" onClick={this.handleSubmit}/>
            </div>
        )
    }
}

export default RequestAccess;