const express = require("express");
const mysql = require("mysql");
const cors = require("cors");

const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const session = require("express-session");

const bcrypt = require("bcrypt");
const saltRounds = 10;

const app = express();

app.use(express.json());
app.use(
  cors({
    origin: ["http://localhost:3000"],
    methods: ["GET", "POST"],
    credentials: true,
  })
);
app.use(cookieParser());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(
  session({
    key: "userId",
    secret: "subscribe",
    resave: false,
    saveUninitialized: false,
    cookie: {
      expires: 60 * 60 * 24,
    },
  })
);

const db = mysql.createConnection({
    protocol:'http:',
    host: "mysql",
    port: 3306,
    user: "root",
    password: "root",
    database: "flowable"
});

app.post("/addApp", (req, res) => {
  const username = req.body.username;
  const app = req.body.app;

    db.query(
      "INSERT INTO ACT_ID_USER_APP (ID_, APP_) VALUES (?,?)",
      [username, app],
      (err, result) => {
        console.log(err);
      }
    );
});

app.post("/getApp", (req, res) => {
    const username = req.body.username;
    const app = req.body.app;

    db.query(
        "SELECT * FROM ACT_ID_USER_APP WHERE ID_ = ? AND APP_ = ?",
        [username, app],
        (err, result) => {
            if (err) {
                res.send({ err: err });
            }
            console.log(result.length)
            if (result.length > 0) {
                //bcrypt.compare(password, result[0].PWD_, (error, response) => {
                //if (response) {
                req.session.user = result;
                console.log(req.session.user);
                res.send(result);
                /*} else {
                  res.send({ message: "Wrong username/password combination!" });
                }*/
            }//);
            else {
                res.send({ message: "No elements found." });
            }
        }
    );
});

app.get("/login", (req, res) => {
  if (req.session.user) {
    res.send({ loggedIn: true, user: req.session.user });
  } else {
    res.send({ loggedIn: false });
  }
});

app.post("/login", (req, res) => {
  const username = req.body.username;
  const password = req.body.password;
    console.log(username)
    console.log(password)
  db.query(
    "SELECT * FROM ACT_ID_USER WHERE ID_ = ? AND PWD_ = ?",
    [username, password],
    (err, result) => {
      if (err) {
        res.send({ err: err });
      }

      if (result.length > 0) {
        //bcrypt.compare(password, result[0].PWD_, (error, response) => {
          //if (response) {
            req.session.user = result;
            console.log(req.session.user);
            res.send(result);
          /*} else {
            res.send({ message: "Wrong username/password combination!" });
          }*/
        }//);
      else {
        res.send({ message: "Wrong username/password combination!" });
      }
    }
  );
});

app.listen(3001, () => {
  console.log("running server");
});
