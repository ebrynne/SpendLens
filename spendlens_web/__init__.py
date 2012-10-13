import os
from flask import Flask

app=Flask(__name__)

os.environ['SPENDLENS_CONFIG'] = './config'
app.config.from_envvar('SPENDLENS_CONFIG')

import spendlens_web.models
import spendlens_web.views






