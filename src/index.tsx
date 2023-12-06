import { NativeModules, Platform } from 'react-native';
import type { Double } from 'react-native/Libraries/Types/CodegenTypes';


const LINKING_ERROR =
  `The package 'react-native-exif-metadata' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Module = NativeModules.ExifMetadata
  ? NativeModules.ExifMetadata
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return Module.multiply(a, b);
}


type metaParams = {
  base64String : string,
  latitude: Double | null,
  longitude: Double | null,
  dateTime: string | null,
  onSuccess : (image:string)=> void
}

export const saveImageMetaInformation = ({
  base64String,
  latitude,
  longitude,
  dateTime,
  onSuccess
}:metaParams) => {
  try {
    Module?.saveBase64intoImage(
      base64String,
      latitude ? latitude : 0.0,
      longitude ? longitude : 0.0,
      dateTime ? dateTime : "00:00",
      onSuccess
    )
  } catch (error) {
    throw Error(String(error));
  }
}

export const getMetaData = ({
  imagePath,
  onSuccess
}: {
    imagePath: string,
    onSuccess: (data:any)=> void
}) => {
  try {
    Module?.getImageExifData(
      imagePath,
      onSuccess
    );
  } catch (error) {
    throw Error(String(error));
  }
}