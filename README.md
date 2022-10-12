## Steps to run Ordering Service

1. Make sure Kitchen is already running.
2. Run the command below:

```
docker compose up --build
```

## Other notes

If you want to run Ordering Service locally, **first read the README from Dining Hall and Client.** 
Next, change the field in the `config.json` file as such:

```json
  "client": "localhost:9001"
```

Then just run it on the local machine. And for the love of God, do not change the ports!