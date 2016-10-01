package com.acronym.base.blocks;

import com.acronym.base.api.materials.MaterialRegistry;
import com.acronym.base.api.materials.MaterialType;
import com.acronym.base.blocks.sets.wood.BlockStorage;
import com.acronym.base.items.ItemBlockOre;
import com.acronym.base.items.ItemBlockStorage;
import com.acronym.base.reference.Reference;
import com.acronym.base.util.FileHelper;
import com.acronym.base.util.Platform;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.tuple.MutablePair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.acronym.base.reference.Reference.tab;

/**
 * Created by Jared on 7/1/2016
 */

/**
 * Remastered by EwyBoy on 7/5/2016
 */
public class BaseBlocks {

    public static Multimap<String, Block> renderMap = ArrayListMultimap.create();
    public static Map<MaterialType, Block> oreBlockMap = new LinkedHashMap<>();
    public static Map<MaterialType, Block> storageBlockMap = new LinkedHashMap<>();


    public static void preInit() throws Exception {
        for (Map.Entry<MutablePair<String, Integer>, MaterialType> entry : MaterialRegistry.getMaterials().entrySet()) {
            if (entry.getValue().isTypeSet(MaterialType.EnumPartType.ORE)) {
                BlockOre ore = new BlockOre(entry.getValue());
                oreBlockMap.put(entry.getValue(), registerBlock(ore, Reference.MODID, "ore_" + entry.getValue().getName().toLowerCase(), "%s Ore", "ore", null, tab, ItemBlockOre.class));
            }
            if (entry.getValue().isTypeSet(MaterialType.EnumPartType.BLOCK)) {
                BlockStorage ore = new BlockStorage(entry.getValue());
                storageBlockMap.put(entry.getValue(), registerBlock(ore, Reference.MODID, "storage_" + entry.getValue().getName().toLowerCase(), "%s Block", "storage", null, tab, ItemBlockStorage.class));
            }
        }
    }

    public static void init() {

    }


    public static void postInit() {
    }

    public static Block registerBlock(Block block, String key, String name) throws Exception {
        return registerBlock(block, Reference.MODID, key, name, key, null, tab, ItemBlock.class);
    }

    public static Block registerBlock(Block block, String key, String name, String texture) throws Exception {
        return registerBlock(block, Reference.MODID, key, name, texture, null, tab, ItemBlock.class);
    }

    public static Block registerBlock(Block block, String key, String name, String texture, Class tile) throws Exception {
        return registerBlock(block, Reference.MODID, key, name, texture, tile, tab, ItemBlock.class);
    }

    public static Block registerBlock(Block block, String key, String name, Class tile) throws Exception {
        return registerBlock(block, Reference.MODID, key, name, tile, tab);
    }

    public static Block registerBlock(Block block, String modid, String key, String name, Class tile, CreativeTabs tab) throws Exception {
        return registerBlock(block, modid, key, name, key, tile, tab, ItemBlock.class);
    }

    public static Block registerBlock(Block block, String modid, String key, String name, String texture, Class tile, CreativeTabs tab, Class<? extends ItemBlock> itemBlock) throws Exception {
        block.setUnlocalizedName(modid + ":" + key).setCreativeTab(tab);

        if (Platform.generateBaseTextures()) {
            writeFile(modid, key, texture);
            writeLangFile(key, name);
        }

        renderMap.put(texture, block);
        Block b = GameRegistry.register(block, new ResourceLocation(Reference.MODID + ":" + key));
        GameRegistry.register(itemBlock.getConstructor(Block.class).newInstance(block), new ResourceLocation(Reference.MODID + ":" + key));

        if (tile != null) {
            GameRegistry.registerTileEntity(tile, key);
        }
        return b;
    }

    private static void writeLangFile(String key, String name) throws IOException {
        File lang = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/lang/en_US.lang");

        if (!lang.exists()) {
            lang.createNewFile();
        }
        Scanner scan = new Scanner(lang);
        List<String> content = new ArrayList<>();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            content.add(line);
        }
        scan.close();
        if (!content.contains((String.format("tile.%s.name=%s", key, name))))
            content.add(String.format("tile.%s.name=%s", key, name));
        FileWriter write = new FileWriter(lang);
        for (String s : content) {
            write.write(s + "\n");
        }
        write.close();
    }

    private static void writeFile(String modid, String key, String texture) throws Exception {
        File baseBlockState = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/blockstates/" + key + ".json");
        File baseBlockModel = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/block/" + key + ".json");
        File baseItem = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");

        FileHelper fileHelper = new FileHelper();

        if (!baseBlockState.exists()) {
            baseBlockState.createNewFile();
            fileHelper.writeFile(baseBlockState, fileHelper.scanFile(modid, key, texture, new File(System.getProperty("user.home") + "/getFluxed/baseBlockState.json")));
        }

        if (!baseBlockModel.exists()) {
            baseBlockModel.createNewFile();
            fileHelper.writeFile(baseBlockModel, fileHelper.scanFile(modid, key, texture, new File(System.getProperty("user.home") + "/getFluxed/baseBlockModel.json")));

        }

        if (!baseItem.exists()) {
            baseItem.createNewFile();
            fileHelper.writeFile(baseItem, fileHelper.scanFile(modid, key, texture, new File(System.getProperty("user.home") + "/getFluxed/baseBlockItem.json")));
        }
    }
}
