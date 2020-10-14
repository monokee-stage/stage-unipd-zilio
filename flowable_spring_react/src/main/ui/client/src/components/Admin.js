import React from "react";
import GetUserTask from "../task/GetUserTask";

class Admin extends React.Component{
    render() {
        return (
            <div>
                <GetUserTask
                    user={this.props.user}
                    isAdmin={this.props.isAdmin}
                    onSignOut={this.props.onSignOut}
                    choice={this.props.choice}
                    app={this.props.app}
                />
            </div>
        )
    }
}

export default Admin;