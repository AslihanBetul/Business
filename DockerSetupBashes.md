# RABBITMQ
```bash
docker run -d --hostname my-rabbit --name some-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=1234 -p 15672:15672 -p 5672:5672 --memory=128m rabbitmq:3-management
```