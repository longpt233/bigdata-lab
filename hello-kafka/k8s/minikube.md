
- k8s
```
minikube delete
minikube start --driver=virtualbox
minikube dashboard

kubectl create namespace kafka
kubectl apply -f 01-zoo.yaml
kubectl port-forward kafka-broker-686d8f4bd4-d45rl 9092 -n kafka


kubectl cluster-info
kubectl cluster-info dump
kubectl get pods -owide
kubectl get node | grep worker | awk '{print $1}'

git config --global alias.tree "log --graph --decorate --pretty=oneline --abbrev-commit"
git config --global alias.tree-all "log --all --graph --decorate --oneline"
git tree
git reset --hard

```

- kafka
```
apt-get install kafkacat
```

- zoo
```
```