terraform {
  backend "s3" {
    bucket = "tf-state-all-in-one"
    key = "state/slidy"
    region = "eu-central-1"
  }
}