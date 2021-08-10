for i in $(seq 1 20)
do
  curl http://localhost:9000/postings > /dev/null
  sleep 1
done
