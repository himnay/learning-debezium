#!/usr/bin/env bash
# Manually (re)register the Debezium orders connector.
# Normally not needed — the connect-init compose service does this automatically.
set -euo pipefail

CONNECT_URL="${CONNECT_URL:-http://localhost:8083}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONNECTOR_FILE="${SCRIPT_DIR}/../debezium/orders-connector.json"

echo "Registering orders-connector at ${CONNECT_URL} ..."
curl -fsS -X POST \
  -H 'Content-Type: application/json' \
  --data @"${CONNECTOR_FILE}" \
  "${CONNECT_URL}/connectors" | jq . 2>/dev/null || true

echo
echo "Connector status:"
curl -fsS "${CONNECT_URL}/connectors/orders-connector/status" | jq . 2>/dev/null
