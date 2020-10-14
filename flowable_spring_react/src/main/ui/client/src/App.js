import React from "react";
import { BrowserRouter as Router, Route } from "react-router-dom";
import "./App.css";
import Registration from "./pages/Registration";
import ChooseApp from "./pages/ChooseApp";

function App() {
  return (
      <Router>
          <Route path="/" exact render={(props) => <Registration />} />
          <Route path="/chooseapp" exact render={(props) => <ChooseApp />} />
      </Router>
  );
}

export default App;