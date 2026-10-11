"""Print an account entry for the private APP_USERS_FILE."""
import argparse
import base64
import getpass
import hashlib
import secrets

parser = argparse.ArgumentParser()
parser.add_argument("username")
parser.add_argument("--id", help="Existing owner/OpenID for migrated records")
parser.add_argument("--role", choices=["staff", "reviewer", "admin"], default="staff")
args = parser.parse_args()
if not args.username.isascii() or not args.username.replace("-", "").replace("_", "").isalnum():
    parser.error("Use letters, numbers, underscore or hyphen")
identity = args.id or args.username
if any(c in identity for c in ",\n\r"):
    parser.error("Invalid identity")
password = getpass.getpass("Password (12+ characters): ")
if len(password) < 12 or password != getpass.getpass("Repeat password: "):
    parser.error("Passwords must match and contain at least 12 characters")
salt = secrets.token_bytes(16)
digest = hashlib.pbkdf2_hmac("sha256", password.encode(), salt, 310000)
print(f"{args.username}={identity},{args.role},{base64.b64encode(salt).decode()},{base64.b64encode(digest).decode()}")
