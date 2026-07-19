
### Compile and deploy image
```bash
mvn clean package
docker build . -t andreuro/mycrawler:latest
docker push andreuro/mycrawler:latest
```

### Run prod
```bash
docker run --name mycrawler --rm -p 127.0.0.1:8050:8080 --network mam-databases-prod_mam-databases-network-prod --network reverse_proxy_network  --env-file .envprod andreuro/mycrawler:latest
```

### Run local 
```bash
docker run --name mycrawler --rm -p 8080:8080 --env-file .envlocaldocker -v mycrawler-browser:/ms-playwright andreuro/mycrawler:latest
```
