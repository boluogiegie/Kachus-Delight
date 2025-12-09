package com.kachudelight.kachu.compat.jei;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.ItemRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * JEI配方类别：蛋包饭升级配方
 * 负责在JEI界面中显示“蛋包饭+番茄酱→香奈美蛋包饭”的合成配方
 * 实现 IRecipeCategory<OmeletteRiceUpgradeRecipe> 接口定义了一个完整的配方显示类别
 */
public class OmeletteRiceUpgradeCategory implements IRecipeCategory<OmeletteRiceUpgradeRecipe> {

    /**
     * 本配方类别的唯一标识类型
     * 用于在JEI系统中注册和识别此类别的配方
     */
    public static final RecipeType<OmeletteRiceUpgradeRecipe> RECIPE_TYPE =
            RecipeType.create(KachuDelight.MOD_ID, "omelette_rice", OmeletteRiceUpgradeRecipe.class);

    // === GUI 颜色常量 ===
    // 调整这些颜色值可以改变配方的整体色调
    private static final int BACKGROUND_COLOR = 0xFFc6c6c6; // 背景颜色（ARGB格式：0xAARRGGBB）
    private static final int BORDER_COLOR = 0xFF555555;     // 边框颜色
    private static final int TEXT_COLOR = 0xFF404040;       // 文字颜色

    // === GUI 尺寸常量 ===
    // 调整这些值可以改变配方显示区域的大小
    private static final int WIDTH = 140;   // 配方区域宽度（像素）
    private static final int HEIGHT = 70;   // 配方区域高度（像素）

    // === 布局位置常量 ===
    // 调整这些坐标可以精确控制每个元素的位置
    private static final int OUTPUT_X = 62;    // 输出槽位的X坐标
    private static final int SLOT_Y = 18;       // 所有槽位的Y坐标（槽位垂直位置）
    private static final int TEXT_Y = 45;       // 说明文字的Y坐标

    // === 成员变量 ===
    private final IDrawable icon;           // 在JEI配方列表侧边栏中显示的图标
    private final Component title;
    private final IDrawable slotBackground; // 配方类别的标题（显示在JEI界面顶部）// 静态箭头图标（从纹理中截取的部分）

    /**
     * 构造函数
     * @param helper JEI提供的GUI助手，用于创建各种绘制元素
     */
    public OmeletteRiceUpgradeCategory(IGuiHelper helper) {
        // 使用香奈美的蛋包饭物品作为图标
        ItemStack iconStack = new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE.get());
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);

        // 标题（可本地化，在lang文件中定义）
        this.title = Component.translatable("jei.kachu.omelette_rice_upgrade");

        // 使用JEI原版工作台箭头
        // 参数说明：
        // - new ResourceLocation("jei", "textures/jei/gui/gui.png")：JEI内置GUI纹理路径
        // - 90：纹理中的起始X坐标（从纹理左上角开始计算）
        // - 168：纹理中的起始Y坐标
        // - 22：截取区域的宽度
        // - 15：截取区域的高度

        // 美化提示1：你可以在这里加载自定义箭头纹理
        // ResourceLocation customArrow = new ResourceLocation(KachuDelight.MOD_ID, "textures/gui/custom_arrow.png");
        // this.arrow = helper.createDrawable(customArrow, 0, 0, 32, 32);
        this.slotBackground = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<OmeletteRiceUpgradeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    /**
     * 设置配方布局：定义输入/输出槽位的位置和内容
     * @param builder 配方布局构建器，用于添加和配置槽位
     * @param recipe 当前要显示的配方数据对象
     * @param focuses JEI的焦点系统，可以获取玩家当前正在查看/搜索的物品
     *                可用于高亮显示相关槽位或条件性显示不同内容
     */
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OmeletteRiceUpgradeRecipe recipe, IFocusGroup focuses) {
//        // 输入：蛋包饭（物品）
//        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, SLOT_Y)
//                .addItemStack(new ItemStack(recipe.inputItem()));
//
//        // 输入：番茄酱
//        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X + 24, SLOT_Y)
//                .addItemStack(recipe.inputSauce().copy());

        // 输出：香奈美的蛋包饭（物品）
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, SLOT_Y)
                .addItemStack(new ItemStack(recipe.outputItem()));

        // 美化提示2：你可以使用 focuses 参数实现动态效果
        // 例如，当玩家聚焦于番茄酱时，高亮显示蛋包饭槽位：
        // if (focuses.getFocuses(VanillaTypes.ITEM_STACK).anyMatch(focus -> focus.getTypedValue().getIngredient().is(ModItems.TOMATO_SAUCE.get()))) {
        //     // 添加一些视觉提示，比如发光边框
        // }
    }

    /**
     * 绘制配方的自定义背景、装饰和文字
     * 这个方法每一帧都会被调用，可以在这里实现动画效果
     *
     * @param recipe 当前绘制的配方数据（当前实现未使用，但可用于条件性绘制）
     *               例如：根据配方不同显示不同的背景或文字
     * @param recipeSlotsView 当前配方槽位的视图对象（当前实现未使用）
     *                        可用于：
     *                        - 获取槽位中当前显示的物品
     *                        - 绘制基于槽位内容的动态效果
     *                        - 检测槽位状态（是否为空、是否有物品等）
     * @param guiGraphics Minecraft的GUI绘制工具，用于执行所有绘制操作
     * @param mouseX 鼠标相对于配方区域左上角的X坐标（当前实现未使用）
     *               可用于：
     *               - 实现悬停效果（鼠标悬停在某个区域时高亮）
     *               - 创建交互式元素（按钮、提示等）
     *               - 鼠标跟随效果
     * @param mouseY 鼠标相对于配方区域左上角的Y坐标（当前实现未使用）
     *               用法同 mouseX
     */
    @Override
    public void draw(OmeletteRiceUpgradeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        // 方案B：与整个GUI区域对齐（留出8像素边距）
        int leftMargin = 3;

        int maxTextWidth = WIDTH - leftMargin - 3;
        String fullText = "shift右键放置蛋包饭，用番茄酱右键浇汁即可获得了喵~";
        List<FormattedCharSequence> lines = font.split(Component.literal(fullText), maxTextWidth);

        int startY = TEXT_Y;
        for (int i = 0; i < lines.size(); i++) {
            guiGraphics.drawString(font, lines.get(i), leftMargin, startY + i * 12, TEXT_COLOR, false);
        }
        slotBackground.draw(guiGraphics, OUTPUT_X - 1, SLOT_Y - 1);
        // 美化提示6：使用 recipeSlotsView 增强文字信息
        // 可以获取槽位中的物品并显示更动态的文字说明
        // 例如：显示当前配方产出的数量或特殊属性

        // 美化提示7：使用 mouseX/mouseY 实现文字悬停效果
        // 检测鼠标是否悬停在文字区域
        // if (mouseX >= textX && mouseX <= textX + textWidth && mouseY >= TEXT_Y && mouseY <= TEXT_Y + 10) {
        //     // 绘制文字背景或改变文字颜色
        //     guiGraphics.fill(textX - 2, TEXT_Y - 2, textX + textWidth + 2, TEXT_Y + 12, 0x80FFFFFF);
        //     guiGraphics.drawString(font, text, textX, TEXT_Y, 0xFF0000FF, false); // 蓝色文字
        // }
    }
}