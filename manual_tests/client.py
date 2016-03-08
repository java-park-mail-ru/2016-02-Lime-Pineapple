from urllib.request import Request, urlopen

REMOTE_HOST = 'localhost'
REMOTE_PORT = 8080
REMOTE_ADDR = 'http://{0}{1}/api/user'.format( REMOTE_HOST , ':'+str(REMOTE_PORT) if REMOTE_PORT else '')

VALUE = """
  {
    "login": "test",
    "password": "test"
  }
"""

HEADERS = {
    'Content-Type': 'application/json'
}

print("[info] SENDING REQUEST TO {0}...".format(REMOTE_ADDR));
request = Request(REMOTE_ADDR, data=VALUE.encode(), headers=HEADERS)
print(">> REQUEST:")
print(repr(request))
print("[info] RECEIVING RESPONSE...")
response_body = urlopen(request).read()
print(">> RESPONSE: ")
print(response_body)
