import ImageKit = require('imagekit');

let ik: ImageKit | undefined;

export function getImageKitInstance() {
  if (!ik) {
    ik = new ImageKit({
      urlEndpoint: process.env.ODE_SEOUL_IMAGE_KIT_URL_ENDPOINT,
      publicKey: process.env.ODE_SEOUL_IMAGE_KIT_PUBLIC_KEY,
      privateKey: process.env.ODE_SEOUL_IMAGE_KIT_PRIVATE_KEY,
    });
  }

  return ik;
}
