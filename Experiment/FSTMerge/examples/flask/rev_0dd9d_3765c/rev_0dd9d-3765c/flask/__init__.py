"""
    flask
    ~~~~~

    A microframework based on Werkzeug.  It's extensively documented
    and follows best practice patterns.

    :copyright: (c) 2011 by Armin Ronacher.
    :license: BSD, see LICENSE for more details.
"""

~~FSTMerge~~ __version__ = '0.8.1-dev' ##FSTMerge## __version__ = '0.8' ##FSTMerge## __version__ = '0.9-dev'

from werkzeug.exceptions import abort
from werkzeug.utils import redirect
from jinja2 import Markup, escape

from .app import Flask, Request, Response
from .config import Config
from .helpers import url_for, jsonify, json_available, flash, \
    send_file, send_from_directory, get_flashed_messages, \
    get_template_attribute, make_response, safe_join
from .globals import current_app, g, request, session, _request_ctx_stack
from .ctx import has_request_context
from .module import Module
from .blueprints import Blueprint
from .templating import render_template, render_template_string

from .signals import signals_available, template_rendered, request_started, \
     request_finished, got_request_exception, request_tearing_down

if json_available:
    from .helpers import json


from .sessions import SecureCookieSession as Session

if json_available:
    from .helpers import json


