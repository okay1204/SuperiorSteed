package me.screescree.SuperiorSteed.superiorhorse.persistenttype;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.reflect.TypeToken;

/**
 * 
 * @author ElgarL
 *
 */
public class PersistentDataType_UUID_ARRAY implements PersistentDataType<byte[], ArrayList<UUID>> {

	@Override
	public Class<byte[]> getPrimitiveType() {
		return byte[].class;
	}

    @SuppressWarnings("unchecked")
	@Override
	public Class<ArrayList<UUID>> getComplexType() {
		return (Class<ArrayList<UUID>>) new TypeToken<ArrayList<UUID>>(){}.getRawType();
	}

	/**
	 * Return a byte[] Array extracted from the Longs in the Array of UUID[].
	 */
	@Override
	public byte[] toPrimitive(ArrayList<UUID> complex, PersistentDataAdapterContext context) {

		ByteBuffer bb = ByteBuffer.wrap(new byte[16 * complex.size()]);
		
		for (UUID uuid : complex) {
			bb.putLong(uuid.getMostSignificantBits());
			bb.putLong(uuid.getLeastSignificantBits());
		}
		
		return bb.array();
	}

	/**
	 * Return an Array of UUIDs constructed from the primitive byte[] data.
	 */
	@Override
	public ArrayList<UUID> fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {

		ArrayList<UUID> uids = new ArrayList<UUID>();
		
		long firstLong;
		long secondLong;
		
		ByteBuffer bb = ByteBuffer.wrap(primitive);
		
		for (int i = 0; i < primitive.length/16; i++) {
			firstLong = bb.getLong();
			secondLong = bb.getLong();
			uids.add(new UUID(firstLong, secondLong));
		}
		return uids;
	}
}
