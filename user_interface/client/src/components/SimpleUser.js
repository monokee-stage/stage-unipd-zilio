import React from "react";
import ChooseApp from "../pages/ChooseApp";

class SimpleUser extends React.Component{
    render() {
        return (
            <ChooseApp
                user={this.props.user}
                isAdmin={this.props.isAdmin}
                onSignOut={this.props.onSignOut}
                choice={this.props.choice}
                app={this.props.app} />
        )
    }
}

export default SimpleUser;