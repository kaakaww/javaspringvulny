#!/usr/bin/env bash

# Request login (XLOGINID) and session (JSESSIONID) cookies from server
curl -k  -c  cookie-jar.txt https://localhost:9000/login-code
# Set local JSESSIONID variable to the JSESSIONID cookie
JSESSIONID=$(awk 'match($0, /JSESSIONID.*/){print substr($0, RSTART + 11, RLENGTH)}' cookie-jar.txt )
# Set local XLOGINID variable to the XLOGINID cookie
XLOGINID=$(awk 'match($0, /XLOGINID.*/){print substr($0, RSTART + 9, RLENGTH)}' cookie-jar.txt)
# Request page with XLOGINID and JSESSIONID cookies and extract the _csrf token
CSRF=$(curl -k  -b  cookie-jar.txt \
    https://localhost:9000/login-form-multi | awk 'match($0,/_csrf".*/) { print substr($0, RSTART+14, RLENGTH -17)}')
# Log into the mutli cooke endpoint using XLOGINID and JSESSIONID cookies and username/password
curl -v -k  \
  -d "_csrf=${CSRF}&loginCode=${XLOGINID}&username=user&password=password&remember=on"  \
  -b  cookie-jar.txt \
  -H  "Content-Type: application/x-www-form-urlencoded" \
  "https://localhost:9000/login-form-multi"

# Run HawkScan injecting local variables as environment variables
hawk scan -e JSESSIONID=${JSESSIONID} -e XLOGINID=${XLOGINID} ./stackhawk.d/stackhawk-multi-cookie-auth.yml

