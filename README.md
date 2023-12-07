# react-native-exif-metadata

This package used to update or get image meta data.

## Installation

## Compatibility

Only support android for now. require react-native >= 0.69

```sh
npm install react-native-exif-metadata
```

## Usage

```js
import { getMetaData,saveImageMetaInformation } from 'react-native-exif-metadata';

await saveImageMetaInformation({
  base64String: "base64 image apth",
  latitude: 01.8797,
  longitude: 14.9098,
  dateTime: String(new Date()),
  onSuccess: (path)=>{
    console.log("image updated path",path);
  }
})

getMetaData({
  imagePath: "image path",
  onSuccess: (imageMetaData)=>{
    console.log("image meta data",imageMetaData);
  }
})
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License


## ComponentName

Supported props.

### Props

| Prop       | Type     | Default | Description                              |
|------------|----------|---------|------------------------------------------|
| latitude   | `double` |  `0.0`  | default value is 0.0                     |
| longitude  | `double` | `0.0`   | default value is 0.0                     |
| dateTime   | `string` |         | time in string                           |