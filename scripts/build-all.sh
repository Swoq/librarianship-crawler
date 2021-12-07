#!/bin/sh
(cd crawler && mvn compile jib:build)
(cd consumer && mvn compile jib:build)
(cd processor && mvn compile jib:build)
