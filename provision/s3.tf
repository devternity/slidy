variable "aws_access_key" {
}

variable "aws_secret_key" {
}



provider "aws" {
  region = "eu-central-1"
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
}

resource "aws_iam_user" "slidy_app" {
  name = "slidy_app"
}

resource "aws_iam_access_key" "slidy_app" {
  user = "${aws_iam_user.slidy_app.name}"
}


resource "aws_iam_user_policy" "slidy_s3_policy" {
  name = "slidy_s3_policy"
  user = "${aws_iam_user.slidy_app.name}"
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": "s3:*",
            "Resource": [
                "arn:aws:s3:::slidy-storage",
                "arn:aws:s3:::slidy-storage/*"
            ]
        }
   ]
}
EOF
}

resource "aws_s3_bucket" "slidy" {
  bucket = "slidy-storage"
  acl = "public-read"

  policy = <<EOF
{
    "Version": "2008-10-17",
    "Statement": [
        {
            "Sid": "PublicReadForGetBucketObjects",
            "Effect": "Allow",
            "Principal": {
                "AWS": "*"
            },
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::slidy-storage/*"
        },
        {
            "Sid": "",
            "Effect": "Allow",
            "Principal": {
                "AWS": "${aws_iam_user.slidy_app.arn}"
            },
            "Action": "s3:*",
            "Resource": [
                "arn:aws:s3:::slidy-storage",
                "arn:aws:s3:::slidy-storage/*"
            ]
        }
    ]
}
EOF
}

output "s3_key" {
  value = "${aws_iam_access_key.slidy_app.id}"
  sensitive = true
}
output "s3_secret" {
  value = "${aws_iam_access_key.slidy_app.secret}"
  sensitive = true
}