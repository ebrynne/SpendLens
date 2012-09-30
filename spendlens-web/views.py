from flask import redirect, url_for, render_template, flash, request
from spendlens-web import app

@app.route("/")
def index():
  return render_template("index.html")

@app.route("/history/")
def history():
  return render_template("history.html")

@app.route("/settings",)
def settings():
  return render_template("settings.html")

@app.route("/login", methods=["POST"])
def login():
  if request.values.get("email", "") in USER_NAMES:
    flash("Logged In")
  else:
    flash("Sorry, we don't remember your username or password quite that way. Want to try again or <a href='%s'>register</a>? " % url_for('register'))
  return redirect(url_for('index'))

@app.route("/register")
def register():
  return render_template("register.html")
