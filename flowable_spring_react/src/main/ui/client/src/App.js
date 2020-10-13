import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import "./App.css";
import Registration from "./pages/Registration";
import ChooseApp from "./pages/ChooseApp";
import AppConfirmed from "./pages/AppConfirmed";
import Main from "./pages/Main";

function App() {
  return (
      <Router>
          <Route path="/" exact render={(props) => <Registration />} />
          <Route path="/main" exact render={(props) => <Main />} />
          <Route path="/chooseapp" exact render={(props) => <ChooseApp />} />
          <Route path="/appConfirmed" exact render={(props) => <AppConfirmed />} />
      </Router>
  );
}

export default App;