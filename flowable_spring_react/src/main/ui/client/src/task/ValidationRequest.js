import React from "react";

class ValidationRequest extends React.Component{

    state = {
        selectedOption: "Yes"
    }

    handleOptionChange = (e) => {
        this.setState({
            selectedOption: e.target.value
        })
    }

    handleFormSubmit = (e) => {
        e.preventDefault()
        //console.log('You have selected:', this.state.selectedOption)
        this.props.validationChoice(this.state.selectedOption)
        fetch("http://localhost:8081/api/" + `${this.state.selectedOption}`, {
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

    render(){
        return (
            <div>
                <p>Do you want to validate the request?</p>
                <div>
                    <form onSubmit={this.handleFormSubmit.bind(this)}>
                        <div>
                            <input type="radio" id="yes" value="Yes" checked={this.state.selectedOption === 'Yes'} onChange={this.handleOptionChange.bind(this)}/>
                                <label htmlFor="yes">Yes</label>
                        </div>
                        <div>
                            <input type="radio" id="no" value="No" checked={this.state.selectedOption === 'No'} onChange={this.handleOptionChange.bind(this)}/>
                                <label htmlFor="no">No</label>
                        </div><br/>
                        <div><input type="submit" value="Confirm" onClick={this.handleFormSubmit.bind(this)}/></div>
                    </form>
                </div>
            </div>
        )
    }
}

export default ValidationRequest;