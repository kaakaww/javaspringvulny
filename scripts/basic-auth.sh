#!/usr/bin/env bash

# Base64 encode the username=user and password=password combo
echo -n "user:password" | base64
