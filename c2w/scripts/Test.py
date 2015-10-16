#!/usr/bin/env python2.7
# -*- coding: utf-8 -*-

import argparse
import subprocess
import os
from c2w.scripts._trial_generic import get_spec
from udp_chat_client import c2wUdpChatClientProtocol
from udp_chat_server import c2wUdpChatServerProtocol






def main():
    protocolclient=c2wUdpChatClientProtocol(DatagramProtocol)
    protocolserver=c2wUdpChatServerProtocol(DatagramProtocol)
    protocolserver.startProtocol()
    protocolclient.startProtocol()


if __name__ == '__main__':
    main()