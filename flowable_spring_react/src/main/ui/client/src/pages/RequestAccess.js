import React, {Component} from "react";

class RequestAccess extends Component{

    handleSubmit = () => {
        console.log(this.props.user + " required access")
        this.props.hasRequiredAccess(true);
    }

    render() {
        return (
            <div>
                <input type="submit" value="Request access" onClick={this.handleSubmit}/>
            </div>
        )
    }
}

export default RequestAccess;