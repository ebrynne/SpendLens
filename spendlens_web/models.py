from flask.ext.sqlalchemy import SQLAlchemy
from datetime import datetime
from spendlens_web import app

db = SQLAlchemy(app)

class User(db.Model):
    id       = db.Column(db.Integer,     primary_key=True)
    username = db.Column(db.String(120), nullable=False)
    password = db.Column(db.String(120), nullable=False)
    email    = db.Column(db.String(120), nullable=False)

    def __init__(self, username, password, email):
        self.username = username
        self.password = password
        self.email    = email

    def __repr__(self):
        return '<User {0}>'.format(self.username)

class Expense(db.Model):
    id       = db.Column(db.Integer,  primary_key=True)
    date     = db.Column(db.DateTime, nullable=False)
    amount   = db.Column(db.Float,    nullable=False)
    tag      = db.Column(db.Integer)
    lat      = db.Column(db.Float)
    lon      = db.Column(db.Float)
    uploaded = db.Column(db.Boolean)

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    user = db.relationship('User',
        backref=db.backref('expenses', lazy='dynamic'))

    def __init__(self, amount, user, tag=None, lat=None, lon=None):
        self.date     = datetime.now()
        self.amount   = amount
        self.user     = user
        self.tag      = tag
        self.lat      = lat
        self.lon      = lon
        self.uploaded = False

    def __repr__(self):
        return '<Expense {0} ${1}>'.format(self.date, self.amount)

class Summary(db.Model):
    id     = db.Column(db.Integer,  primary_key=True)
    date   = db.Column(db.DateTime, nullable=False)
    spent  = db.Column(db.Float,    nullable=False)
    budget = db.Column(db.Float,    nullable=False)

    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    user = db.relationship('User',
        backref=db.backref('summaries', lazy='dynamic'))

    def __init__(self, spent, budget, user):
        self.date   = datetime.now()
        self.spent  = spent
        self.budget = budget
        self.user   = user

    def __repr__(self):
        return '<Summary {0} ${1}>'.format(self.date.date(), self.spent)

def create_test_data():
    evan = User('evanp', 'komodoed', 'epurcer@gmail.com')
    for i in range(1, 11):
        db.session.add(Expense(i+0.25, evan))
    db.session.add(evan)
    db.session.commit()
