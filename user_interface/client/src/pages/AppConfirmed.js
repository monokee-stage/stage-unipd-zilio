import React from "react";
import Axios from "axios";

class AppConfirmed extends React.Component{

    state = {
        listItems: [],
        updated: false
    }

    addApp = (app) => {
        Axios.post("http://localhost:3001/addApp", {
            username: this.props.user,
            app: app,
        }).then((response) => {
            if(response.data.message)
                console.log(response)
            else {
                console.log(response)
            }
        });
    }

    updateListItem = () => {
        if(this.props.found === false) {
            this.state.listItems.push(this.props.app);
            this.state.updated = true
            this.addApp(this.props.app);
            //this.props.listApp(this.state.listItems);;
        }else {
            this.state.updated = false;
        }
        console.log("this.state.updated: " + this.state.updated)
    }

    render(){
        this.updateListItem();
        return (
            <div>
                {
                    (this.state.updated === true) ?
                        <div>
                            <p>You have now access to the following app: <h3>{this.props.app}</h3></p>
                            <ul className="list-group">
                                {this.state.listItems.map(listItems => (
                                    <li className="list-group-item list-group-item-primary">
                                        {listItems}
                                    </li>
                                ))}
                            </ul>
                        </div>
                        :
                        <div>
                            <p style={{color: "red"}}>You've already added this application.</p>
                        </div>
                }
            </div>
        )
    }
}

export default AppConfirmed;