import * as fs from 'fs';
import * as dayjs from 'dayjs';
import { getImageKitInstance } from './imagekit';

export async function createImageKitUploadProvider() {
  const BaseProvider = (await import('@adminjs/upload')).BaseProvider;

  class ImageKitUploadProvider extends BaseProvider {
    constructor() {
      super('');
    }

    async upload(file: any, key: string): Promise<any> {
      const fileBuffer = await fs.promises.readFile(file.path);

      return getImageKitInstance().upload({
        file: fileBuffer,
        folder: '/ode-seoul/',
        fileName: key.split('/').at(-1),
        useUniqueFileName: false,
        customMetadata: {
          originalFileName: file.name,
        },
      });
    }

    async delete(key: string, bucket: string): Promise<any> {
      return;
    }

    async path(key: string, bucket: string): Promise<string> {
      return key;
    }
  }

  return new ImageKitUploadProvider();
}

export function imageKitUploadPathFunction(record, filename) {
  return getImageKitInstance().url({
    path: `/ode-seoul/img_${dayjs().format('YYYYMMDDHHmmssSSS')}`,
    urlEndpoint: process.env.ODE_SEOUL_IMAGE_KIT_URL_ENDPOINT,
    transformation: [
      {
        width: '720',
        height: '720',
        crop: 'at_max',
      },
    ],
  });
}
