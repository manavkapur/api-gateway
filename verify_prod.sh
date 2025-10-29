#!/bin/zsh
echo "🚀 Supreme Solutions — Production Verification (Render + Eureka + Gateway)"
echo "-------------------------------------------------------------"

# =============================
# Step 1 — Eureka Server
# =============================
echo "🔹 Checking Eureka Server..."
curl -s -o /dev/null -w "%{http_code} ✅ Eureka Server\n" https://eureka-server-sk4j.onrender.com

# =============================
# Step 2 — Direct Health Checks (Each Service)
# =============================
echo ""
echo "🔹 Checking microservice health (direct)..."

services=(
  "quote-service https://quote-service-rscn.onrender.com"
  "user-service https://user-service-ycvt.onrender.com"
  "contact-service https://contact-service-0oxu.onrender.com"
  "email-service https://email-service-m4wm.onrender.com"
  "notification-service https://notification-service-ln2b.onrender.com"
  "channel-server https://channel-server-huh9.onrender.com"
)

for service in "${services[@]}"; do
  name=${service%% *}
  url=${service#* }
  health_url="${url}/actuator/health"
  code=$(curl -s -o /dev/null -w "%{http_code}" "$health_url")
  if [ "$code" = "200" ]; then
    echo "✅ $name is UP (HTTP $code)"
  else
    echo "❌ $name failed (HTTP $code)"
  fi
done

# =============================
# Step 3 — Eureka App Registry
# =============================
echo ""
echo "🔹 Checking Eureka registered apps..."
curl -s https://eureka-server-sk4j.onrender.com/eureka/apps | grep -E 'APPLICATION|STATUS' | sed 's/<[^>]*>//g'

# =============================
# Step 4 — API Gateway Health (Custom Domain)
# =============================
echo ""
echo "🔹 Checking API Gateway routes..."

gateway="https://api.supremebuildsolutions.com"
routes=(
  "user-service $gateway/user-service/actuator/health"
  "quote-service $gateway/quote-service/actuator/health"
  "contact-service $gateway/contact-service/actuator/health"
  "notification-service $gateway/notification-service/actuator/health"
  "email-service $gateway/email-service/actuator/health"
  "channel-server $gateway/channel-server/actuator/health"
)

for route in "${routes[@]}"; do
  name=${route%% *}
  url=${route#* }
  code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
  if [ "$code" = "200" ]; then
    echo "✅ $name via Gateway is UP (HTTP $code)"
  else
    echo "⚠️  $name via Gateway not responding (HTTP $code)"
  fi
done

echo ""
echo "🎯 Verification complete!"
echo "If all show ✅ HTTP 200, your Render + Neon + Upstash + Eureka setup is fully operational."

