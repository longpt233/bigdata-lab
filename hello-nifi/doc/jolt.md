# Jolt (JsOn Language for Transform)

Input JSON -> spec.json -> JSON Output

# Type 
```
shift       : copies data from input to the output tree
default     : applies default values to the tree.
remove      : removes data from the tree.
sort        : sort the Map key values alphabetically.
cardinality : adjusts the cardinality of input data.
```

# Tools

https://jolt-demo.appspot.com/#inception
https://www.linkedin.com/pulse/jolt-transformation-overview-saurav-daruka/

# shift

```json
[{
    "operation": "shift",
    "spec": {
      "time_create": "time_group.time_create",
      "click_or_view": {
        "0": {
          "#false": "click_or_view_on_false"
        },
        "1": {
          "#true": "click_or_view"
        }
      },
      "*": "&"  # còn lại giữ nguyên
    }
  }
]
```

```json
{
  "time_create": 11,
  "click_or_view": 0
}

```
```json
{
  "time_group": {
    "time_create": 11
  },
  "click_or_view_on_false": "false"
}
```

# modify-overwrite-beta

```
{
  "itemInfo": "1|2",
  "default": 1,
}

jolt.json
[
  {
    "operation": "modify-overwrite-beta",
    "spec": {
      "itemInfo": "=split('\\|',@(1,itemInfo))",
      "infoSize": "=size(@(1,itemInfo))",
      "infoDataFirst": "=elementAt(0,@(1,itemInfo))"
    }
  },
  {
    "operation": "modify-overwrite-beta",
    "spec": {
      "infoDataFirst": ["=toInteger", 0],
      "add_info": ["=toLong", 0]
    }
  },
]

{
  "itemInfo" : [ "1", "2" ],
  "default" : 1,
  "infoSize" : 2,
  "infoDataFirst" : 1,
  "add_info" : 0
}

```

# remove

```
{
  "remove": 1
}

jolt.json
[
  {
    "operation": "remove",
    "spec": {
      "remove": "",
      "blank*": ""
    }
  }
]

{
}

```

# valid code check

```
import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import java.io.*;

public class MainClass {
    public static void main (String [] args) throws Exception {
        Schema schema = new Schema.Parser().parse("{\n" +
                "     \"type\": \"record\",\n" +
                "     \"namespace\": \"com.acme\",\n" +
                "     \"name\": \"Test\",\n" +
                "     \"fields\": [\n" +
                "       { \"name\": \"name\", \"type\": \"string\" },\n" +
                "       { \"name\": \"age\", \"type\": \"int\" },\n" +
                "       { \"name\": \"sex\", \"type\": \"string\" },\n" +
                "       { \"name\": \"active\", \"type\": \"boolean\" }\n" +
                "     ]\n" +
                "}");
        String json = "{\"name\":\"alex\",\"age\":23,\"sex\":\"M\",\"active\":true}";
        System.out.println(validateJson(json, schema));
        String invalidJson = "{\"name\":\"alex\",\"age\":23,\"sex\":\"M\"}"; // missing active field
        System.out.println(validateJson(invalidJson, schema));
    }

    public static boolean validateJson(String json, Schema schema) throws Exception {
        InputStream input = new ByteArrayInputStream(json.getBytes());
        DataInputStream din = new DataInputStream(input);

        try {
            DatumReader reader = new GenericDatumReader(schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
            reader.read(null, decoder);
            return true;
        } catch (AvroTypeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
```