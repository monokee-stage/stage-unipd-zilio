import React, { useEffect, useState } from "react";
import Axios from "axios";

import SimpleUser from "../components/SimpleUser";
import Admin from "../components/Admin";

export default function Main() {
  const [role, setRole] = useState("");

  Axios.defaults.withCredentials = true;
  useEffect(() => {
    Axios.get("http://localhost:3001/login").then((response) => {
      if (response.data.loggedIn === true) {
        if(response.data.user[0].REV_ === "1")
          setRole("admin");
        else if (response.data.user[0].REV_ === "2")
          setRole("simple_user");
      }
    });
  }, []);

  return (
    <div>
      {role === "simple_user" && <SimpleUser />}
      {role === "admin" && <Admin />}
    </div>
  );
}
