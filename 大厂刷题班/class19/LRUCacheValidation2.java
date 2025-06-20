package class19;

public class LRUCacheValidation2 {
    public static void main(String[] args) {
        // 创建容量为3的LRU缓存
        Code01_LRUCacheEdition2 cache = new Code01_LRUCacheEdition2(3);

        System.out.println("===== 初始状态 =====");
        printCacheState(cache);

        // 添加前3个元素
        System.out.println("\n===== 添加元素1-3 =====");
        cache.put(1, 10); // 缓存: [1=10]
        cache.put(2, 20); // 缓存: [1=10, 2=20]
        cache.put(3, 30); // 缓存: [1=10, 2=20, 3=30]
        printCacheState(cache);

        // 访问元素1和2（提升为最近使用）
        System.out.println("\n===== 访问元素1和2 =====");
        System.out.println("get(1) = " + cache.get(1)); // 返回10 (LRU顺序: 3,2,1 -> 3,1,2)
        System.out.println("get(2) = " + cache.get(2)); // 返回20 (LRU顺序: 3,1,2 -> 3,2,1)
        printCacheState(cache);

        // 添加新元素4触发淘汰（淘汰最久未使用的元素3）
        System.out.println("\n===== 添加元素4触发淘汰 =====");
        cache.put(4, 40); // 淘汰3，缓存: [1=10, 2=20, 4=40]
        System.out.println("get(3) = " + cache.get(3)); // 应返回-1（已被淘汰）
        printCacheState(cache);

        // 更新现有元素2（提升为最近使用）
        System.out.println("\n===== 更新元素2的值 =====");
        cache.put(2, 200); // 更新值，缓存: [1=10, 4=40, 2=200]（LRU顺序: 1,4,2 -> 1,4,2）
        printCacheState(cache);

        // 添加新元素5触发淘汰（淘汰最久未使用的元素1）
        System.out.println("\n===== 添加元素5触发淘汰 =====");
        cache.put(5, 50); // 淘汰1，缓存: [4=40, 2=200, 5=50]
        System.out.println("get(1) = " + cache.get(1)); // 应返回-1（已被淘汰）
        printCacheState(cache);

        // 访问元素4（提升为最近使用）
        System.out.println("\n===== 访问元素4 =====");
        System.out.println("get(4) = " + cache.get(4)); // 返回40 (LRU顺序: 2,5,4 -> 2,5,4)
        printCacheState(cache);

        // 添加新元素6触发淘汰（淘汰最久未使用的元素2）
        System.out.println("\n===== 添加元素6触发淘汰 =====");
        cache.put(6, 60); // 淘汰2，缓存: [5=50, 4=40, 6=60]
        System.out.println("get(2) = " + cache.get(2)); // 应返回-1（已被淘汰）
        printCacheState(cache);

        // 最终缓存状态
        System.out.println("\n===== 最终缓存状态 =====");
        System.out.println("get(4) = " + cache.get(4)); // 返回40
        System.out.println("get(5) = " + cache.get(5)); // 返回50
        System.out.println("get(6) = " + cache.get(6)); // 返回60
    }

    // 辅助方法：打印缓存状态
    private static void printCacheState(Code01_LRUCacheEdition2 cache) {
        // 注意：实际实现中无法直接访问内部状态
        // 这里只是为了演示，实际测试需要调用get方法验证
        System.out.println("当前缓存状态（LRU顺序）:");
        System.out.println("  访问4: " + cache.get(4));
        System.out.println("  访问5: " + cache.get(5));
        System.out.println("  访问6: " + cache.get(6));
        System.out.println("  访问1: " + cache.get(1));
        System.out.println("  访问2: " + cache.get(2));
        System.out.println("  访问3: " + cache.get(3));
    }
}
