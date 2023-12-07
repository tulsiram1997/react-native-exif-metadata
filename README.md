# react-native-exif-metadata

This package used to update or get image exif data.

## Installation

```sh
npm install react-native-exif-metadata
```

## Usage

```js
import { getMetaData,saveImageMetaInformation } from 'react-native-exif-metadata';

// ...
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

Description of your component.

### Props

| Prop       | Type     | Default | Description                              |
|------------|----------|---------|------------------------------------------|
| prop1      | `string` |         | Explanation of prop1                     |
| prop2      | `number` | `0`     | Explanation of prop2 with a default value|
| prop3      | `bool`   | `false` | Explanation of prop3 

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
