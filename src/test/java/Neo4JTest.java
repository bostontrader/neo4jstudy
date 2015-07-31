import junit.framework.TestCase;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4JTest extends TestCase {

    private static enum RelTypes implements RelationshipType
    {
        KNOWS
    }

    public void testNeo4J() {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( "c:\\testneo" );
        registerShutdownHook(graphDb);

        Node firstNode;
        Node secondNode;
        Relationship relationship;

        try ( Transaction tx = graphDb.beginTx() )
        {

            firstNode = graphDb.createNode();
            firstNode.setProperty( "message", "Hello, " );
            secondNode = graphDb.createNode();
            secondNode.setProperty( "message", "World!" );

            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "brave Neo4j " );

            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );

            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            tx.success();
        }


        graphDb.shutdown();
    }
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }

    // Set properties via properties file
    /*GraphDatabaseService graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder( storeDir )
            .loadPropertiesFromFile( pathToConfig + "neo4j.properties" )
            .newGraphDatabase();

    // Set properties programmatically
    GraphDatabaseService graphDb = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder( storeDir )
            .setConfig( GraphDatabaseSettings.pagecache_memory, "512M" )
            .setConfig(GraphDatabaseSettings.string_block_size, "60")
            .setConfig(GraphDatabaseSettings.array_block_size, "300")
            .newGraphDatabase();

    // Read only
    graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
            "target/read-only-db/location" )
    .setConfig( GraphDatabaseSettings.read_only, "true" )
    .newGraphDatabase();




*/
}