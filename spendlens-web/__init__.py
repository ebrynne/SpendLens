from flask import Flask

app=Flask(__name__)
app.config.from_envvar('config', silent=True)

import spendlens-web.models
import spendlens-web.views






