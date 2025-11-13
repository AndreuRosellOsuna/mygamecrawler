docker build . -t andreuro/mycrawler:0.0.2

docker push andreuro/mycrawler:0.0.2

docker run --name mycrawler --rm -p 127.0.0.1:8050:8080 --network mam-databases-prod_mam-databases-network-prod --network reverse_proxy_network andreuro/mycrawler:0.0.2

