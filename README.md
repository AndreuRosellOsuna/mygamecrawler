```bash
mvn clean package
```

```bash
docker build . -t andreuro/mycrawler:latest
```

```bash
docker push andreuro/mycrawler:latest
```

```bash
//TODO use a volume here ms-playwright
docker run --name mycrawler --rm -p 127.0.0.1:8050:8080 --network mam-databases-prod_mam-databases-network-prod --network reverse_proxy_network  --env-file .envprod andreuro/mycrawler:latest
```