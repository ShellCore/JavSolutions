# Jav Solutions

Java

## Definición

Jav Solutions Corporation, es una "**tienda central de ficción**" que recibe las peticiones mandados por las tiendas generales y genera reportes basado en el contenido. Éste tipo de reportes proveé a las tiendas generales con estrategias y soluciones para ayudar a incrementar sus ventas en el mercado.

Mientras JavSolutions es una **tienda central** mediana con pocos empleados, se encuentra gestionando por una gran cantidad de **tiendas generales**. Cada tienda general tiene administradores de mercado y ventas, y ellos usan los reportes de análisis de ventas para mostrar las tendencias que ocurren en los volumenes de ventas de la companía a traves del tiempo. En su forma más básica, un reporte de análisis de ventas muestra si las ventas han aumentado o disminuido. Los administradores de ventas analizan las tendencias en el reporte para determinar el mejor curso de acción. Ellos usan los reportes también para identificar las oportunidades de mercado y áreas donde se pudiéra incrementar el volumen.

Aunque el departamento de informática trabaja relativamente bien generando éste tipo de reportes, necesitan ahcerlo de una mejor y más rápida forma para cumplir con las fechas límite emitido por los administradores de mercado y ventas.

Cada **Tienda general** genera sus propias facturas, y la **tienda central** recibe las facturas enviadas por los anteriores. Las **tiendas generales** tiene las facturas almacenadas en un archivo con formato XML o JSON y necesitan enviarlos a la tienda central, que los usará para generar los reportes basado en el contenido.

## Descripción del problema

#### Recuerda que tenemos la siguiente situación:

Tenemos una gran cantidad de tiendas generales que generan sus propias facturas, y la tienda central que recibe las facturas enviadas por los primeros. Las tiendas generales tenen sus propias facturas guardadas en formato XML o JSON y requieren ser enviados a la tienda central, que los utililzará para generar los reportes basados en el contenido.

#### Tienda central (server)

La tienda central estará corriendo un programa que actuará como un servidor y recibirá conexiones de las tiendas generales, recibira el contenido de las facturas, y los almacenará localmente para que puedan ser usados para generar reportes mas adelante.

Después de que la conexión se establezca, si el mensaje NEWFILE es recibida, significa que la tienda general enviará una nueva factura a la tienda central. El siguiente mensaje después de "NEWFILE" no es parte de la factura, pero es el nombre del archivo que sera usado para guardar la factura.

Entonces, los siguientes mensajes contendrán el contenido de la factura. Cuando el mensaje "ENDFILE" sea recibido, significa que el contenido de la factura ha terminado. Si la tienda central recibe un mensaje nulo, o el mensaje "CONNECTIONEND", significa que la tienda general quiere detener la comunicación.

Cada factura será almacenado automáticamente en un archivo separado.

El programa de la tienda central deberá tener la habilidad de leer la entrada del usuario desde el teclado (incluso en medio de la recepción de una factura de un programa cliente). Si el usuario ingresa el commando "REPORT", el programa revisará el la cantidad total de ingresos de cada factura, y los desplegará en la pantalla.

Si el usuario ingresa el commando "DELETE", el programa borrará las facturas almacenadas. Si el usuario ingresa el comando "CLOSE", el programa deberá cerrar todas las conexiones y luego terminará.

#### Tienda general (cliente)

Cada tienda general estará corriendo un programa que actuará como un cliente y se conectará con el servidor de la tienda central. Muchos de estos programas deberán poderse conectar con el servidor al mismo tiempo.

El programa cliente debe de tener la habilidad de leer la entrada del usuario desde el teclado. Cuando el programa comienza, le pregunta al usuario la IP y el puerto del servidor, y establece la conexión.

Despues de eso, si el usuario ingresa el comando "SEND invoice.txt", el archivo "invoice.txt" deberá ser enviado al servidor.

#### Formato de facturas

Puedes utilizar tanto el formato XML o JSON para las facturas. El formato es el que sigue:

**XML**
```
<invoice>
    <products>
        <product>
            <name>Food</name>
            <price>5.00</price>
        </product>
        <product>
            <name>Drink</name>
            <price>2.00</price>
        </product>
    </products>
    <total>7.00</total>
</invoice>
```

**JSON**
```
{
    "invoice":{
        "products":[
            {"name":"Food", "price":"5.00"}
            {"name":"Drink", "price":"2.00"}
        ],
        "total":"7.00"
    }
}
```

El cliente no tiene necesidad de analizarlo gramáticamente, solo enviarlo al servidor. El servidor debe de tener la habilidad de analizar el documento completamente, pero solo se va a usar la información identificada como el total.

#### Desafío

Además de computar el total cuando los usuarios ingresan el comando "REPORT", también debe de computar el item más popular. Determina cual estructura de datos podría ser la mejor para mantener el seguimiento de los items que aparecen en cada reporte, luego imprime en pantalla el item más popular de todas las facturas.

## Fuente

**Jav Solutions** es el ejercicio final para entregar como parte de los requerimientos del curso [Java Fundamentals for Android Development](https://courses.edx.org/courses/course-v1:GalileoX+CAAD001X+1T2017/info) de la plataforma educativa [Edx](https://www.edx.org/).