import React from "react";

class WaitingAdminApproval extends React.Component{

    render() {
        return (
            <div>
                <h3>This app needs the Admin's approval.</h3>
                <p>Your request has been sent to the Admin.</p>
                <ul className="list-group">
                    {this.props.listApp.map(listItems => (
                        <li className="list-group-item list-group-item-primary">
                            {listItems}
                        </li>
                    ))}
                </ul>
            </div>
        )
    }
}

export default WaitingAdminApproval;