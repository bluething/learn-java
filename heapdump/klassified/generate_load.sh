for i in $(seq 1 10000)
do
  curl http://localhost:9000/postings > /dev/null
done
