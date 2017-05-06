variable "do_token" {
}

variable "droplet_ssh_pub" {
  default = "./droplet_rsa.pub"
}

provider "digitalocean" {
  token = "${var.do_token}"
}

resource "digitalocean_droplet" "slidy" {
  image = "ubuntu-17-04-x64"
  name = "slidy"
  size = "1gb"
  region = "fra1"
  ssh_keys = [
    "${digitalocean_ssh_key.provision.fingerprint}"
  ]
}

resource "digitalocean_ssh_key" "provision" {
  name = "A key for automated provisioning"
  public_key = "${file(var.droplet_ssh_pub)}"
}

resource "digitalocean_floating_ip" "public_ip" {
  droplet_id = "${digitalocean_droplet.slidy.id}"
  region = "${digitalocean_droplet.slidy.region}"
}

output "slidy_ip" {
  value = "${digitalocean_floating_ip.public_ip.ip_address}"
}
