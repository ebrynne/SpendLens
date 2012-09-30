from flask.ext.sqlalchemy import SQLAlchemy
from spendlens-web import app

db = SQLAlchemy(app)

class Expense(db.model):
    id = db.Column(db.Integer, primary_key=True)

    def __init__(self, username, email):
        self.username = username
        self.email = email

class Summary(db.model):
    id = db.Column(db.Integer, primary_key=True)

    def __init__(self, username, email):
        self.username = username
        self.email = email

class Budget(db.model):
    id = db.Column(db.Integer, primary_key=True)

    def __init__(self, username, email):
        self.username = username
        self.email = email
