<template>
  <div class="wrapper">
    <!-- 后退按钮 -->
    <div class="back-btn" @click="back" aria-label="返回">
      <i class="fa fa-arrow-left"></i>
    </div>

    <!-- 页面标题 -->
    <header>
      <p>管理商家</p>
    </header>

    <!-- 功能卡片容器 -->
    <div class="card-container">
      <!-- 我的商家列表 -->
      <div class="section">
        <div class="section-title">
          <i class="fa fa-building"></i> 我的商家
          <span class="badge">{{ myBusinesses.length }}</span>
        </div>
        <div class="business-list">
          <div class="business-card" v-for="b in myBusinesses" :key="b.businessId" @click="selectBusiness(b)">
            <div class="business-info">
              <div class="business-name">{{ b.businessName }}</div>
              <div class="business-address">{{ b.businessAddress || '暂无地址' }}</div>
              <div class="business-meta">
                <span class="meta-item">
                  <i class="fa fa-tag"></i> 起送:￥{{ b.starPrice }}
                </span>
                <span class="meta-item">
                  <i class="fa fa-truck"></i> 配送:￥{{ b.deliveryPrice }}
                </span>
              </div>
            </div>
            <i class="fa fa-angle-right arrow-icon"></i>
          </div>
          <div v-if="myBusinesses.length === 0" class="empty-tip">
            暂未拥有商家，请点击下方新增商家
          </div>
        </div>
      </div>

      <!-- 商家详情和食品管理 -->
      <div class="section" v-if="selectedBusiness">
        <div class="section-title">
          <i class="fa fa-cutlery"></i> {{ selectedBusiness.businessName }} - 食品管理
        </div>

        <!-- 操作按钮 -->
        <div class="action-bar">
          <button class="action-btn add-btn" @click="showFoodForm = true; isEditingFood = false; resetFoodForm();">
            <i class="fa fa-plus"></i> 新增食品
          </button>
        </div>

        <!-- 食品列表 -->
        <div class="food-list" v-if="!showFoodForm">
          <div class="food-card" v-for="f in businessFoods" :key="f.foodId">
            <div class="food-info">
              <div class="food-header">
                <div class="food-name">{{ f.foodName }}</div>
                <div class="food-price">￥{{ f.foodPrice }}</div>
              </div>
              <div class="food-desc">{{ f.foodExplain || '暂无描述' }}</div>
            </div>
            <div class="food-actions">
              <button class="edit-btn" @click="editFood(f)">
                <i class="fa fa-edit"></i> 编辑
              </button>
              <button class="delete-btn" @click="deleteFood(f.foodId)">
                <i class="fa fa-trash"></i> 删除
              </button>
            </div>
          </div>
          <div v-if="businessFoods.length === 0" class="empty-tip">
            暂无食品，点击上方按钮添加
          </div>
        </div>

        <!-- 新增/编辑食品表单 -->
        <div class="food-form" v-if="showFoodForm">
          <div class="form-title">{{ isEditingFood ? '编辑食品' : '新增食品' }}</div>

          <div class="form-item">
            <label class="form-label">食品名称 *</label>
            <input class="form-input" v-model="foodForm.foodName" placeholder="请输入食品名称" maxlength="50">
          </div>

          <div class="form-item">
            <label class="form-label">食品说明</label>
            <textarea class="form-textarea" v-model="foodForm.foodExplain" placeholder="请简要描述食品特点" rows="2"></textarea>
          </div>

          <div class="form-item">
            <label class="form-label">图片地址</label>
            <input class="form-input" v-model="foodForm.foodImg" placeholder="请输入图片URL">
          </div>

          <div class="form-item">
            <label class="form-label">价格 *</label>
            <input class="form-input form-input-small" v-model="foodForm.foodPrice" type="number" step="0.01" placeholder="价格">
          </div>

          <div class="form-item">
            <label class="form-label">备注</label>
            <input class="form-input" v-model="foodForm.remarks" placeholder="可选备注">
          </div>

          <div class="form-buttons">
            <button class="cancel-btn" @click="showFoodForm = false">取消</button>
            <button class="submit-btn" @click="submitFood">保存</button>
          </div>
        </div>
      </div>

      <!-- 功能操作卡片 -->
      <div class="action-cards" v-if="!selectedBusiness">
        <!-- 新增商家卡片 -->
        <div class="action-card" @click="toAddBusiness">
          <div class="card-icon">
            <i class="fa fa-plus"></i>
          </div>
          <div class="card-content">
            <div class="card-title">新增商家</div>
            <div class="card-desc">创建并管理新的商家信息</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getSessionStorage } from "@/common";

export default {
  name: "AddBusinessPage",
  data() {
    return {
      user: {},
      myBusinesses: [],
      selectedBusiness: null,
      businessFoods: [],
      showFoodForm: false,
      isEditingFood: false,
      foodForm: {
        foodId: null,
        foodName: "",
        foodExplain: "",
        foodImg: "",
        foodPrice: "",
        remarks: ""
      }
    };
  },
  mounted() {
    this.loadUser();
    this.loadMyBusinesses();
  },
  methods: {
    back() {
      if (this.selectedBusiness) {
        this.selectedBusiness = null;
        this.showFoodForm = false;
      } else {
        this.$router.back();
      }
    },
    loadUser() {
      const rawUser = getSessionStorage('user');
      this.user = typeof rawUser === 'string' ? JSON.parse(rawUser) : (rawUser || {});
    },
    async loadMyBusinesses() {
      try {
        const res = await this.$axios.get("BusinessController/listBusinessByOrderTypeId/1");
        if (res.data.code === 200) {
          const allBusinesses = res.data.result || [];
          this.myBusinesses = allBusinesses.filter(b => b.ownerId === this.user.userId);
        }
      } catch (e) {
        console.error("加载商家列表失败", e);
      }
    },
    async selectBusiness(business) {
      this.selectedBusiness = business;
      this.showFoodForm = false;
      await this.loadBusinessFoods();
    },
    async loadBusinessFoods() {
      if (!this.selectedBusiness) return;
      try {
        const res = await this.$axios.get(`FoodController/listFoodByBusinessId/${this.selectedBusiness.businessId}`);
        if (res.data.code === 200) {
          this.businessFoods = res.data.result || [];
        }
      } catch (e) {
        console.error("加载食品列表失败", e);
      }
    },
    toAddBusiness() {
      this.$router.push({ path: "/addBusiness" });
    },
    resetFoodForm() {
      this.foodForm = {
        foodId: null,
        foodName: "",
        foodExplain: "",
        foodImg: "",
        foodPrice: "",
        remarks: ""
      };
    },
    editFood(food) {
      this.isEditingFood = true;
      this.foodForm = {
        foodId: food.foodId,
        foodName: food.foodName,
        foodExplain: food.foodExplain || "",
        foodImg: food.foodImg || "",
        foodPrice: food.foodPrice,
        remarks: food.remarks || ""
      };
      this.showFoodForm = true;
    },
    async submitFood() {
      if (!this.foodForm.foodName.trim()) {
        alert("食品名称不能为空！");
        return;
      }
      if (!this.foodForm.foodPrice || isNaN(Number(this.foodForm.foodPrice))) {
        alert("请输入有效的价格！");
        return;
      }

      try {
        let res;
        if (this.isEditingFood) {
          res = await this.$axios.put("FoodController/updateFood", {
            foodId: this.foodForm.foodId,
            foodName: this.foodForm.foodName.trim(),
            foodExplain: this.foodForm.foodExplain.trim() || "",
            foodImg: this.foodForm.foodImg.trim() || "",
            foodPrice: Number(this.foodForm.foodPrice),
            businessId: this.selectedBusiness.businessId,
            remarks: this.foodForm.remarks.trim() || ""
          });
        } else {
          res = await this.$axios.post("FoodController/saveFood", {
            foodName: this.foodForm.foodName.trim(),
            foodExplain: this.foodForm.foodExplain.trim() || "",
            foodImg: this.foodForm.foodImg.trim() || "",
            foodPrice: Number(this.foodForm.foodPrice),
            businessId: this.selectedBusiness.businessId,
            remarks: this.foodForm.remarks.trim() || ""
          });
        }

        if (res.data.code === 200) {
          alert(this.isEditingFood ? "修改成功！" : "添加成功！");
          this.showFoodForm = false;
          await this.loadBusinessFoods();
        } else {
          alert("操作失败：" + (res.data.message || "未知错误"));
        }
      } catch (e) {
        alert("操作失败，请重试！");
        console.error(e);
      }
    },
    async deleteFood(foodId) {
      if (!confirm("确定要删除该食品吗？")) return;

      try {
        const res = await this.$axios.delete(`FoodController/deleteFood/${foodId}`);
        if (res.data.code === 200) {
          alert("删除成功！");
          await this.loadBusinessFoods();
        } else {
          alert("删除失败：" + (res.data.message || "未知错误"));
        }
      } catch (e) {
        alert("删除失败，请重试！");
        console.error(e);
      }
    }
  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-top: 12vw;
  padding-bottom: 5vw;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

header {
  width: 100%;
  height: 12vw;
  background-color: #0097FF;
  color: #fff;
  font-size: 4.8vw;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.back-btn {
  cursor: pointer;
  width: 8vw;
  height: 8vw;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  left: 4vw;
  top: 2vw;
  z-index: 1001;
  color: white;
  font-size: 5vw;
}

.card-container {
  width: 100%;
  padding: 4vw;
}

.section {
  margin-bottom: 5vw;
}

.section-title {
  font-size: 4vw;
  font-weight: 600;
  color: #333;
  margin-bottom: 3vw;
  display: flex;
  align-items: center;
  gap: 2vw;
}

.section-title i {
  color: #0097FF;
}

.badge {
  background-color: #0097FF;
  color: white;
  border-radius: 10vw;
  padding: 0.3vw 2vw;
  font-size: 3vw;
  font-weight: normal;
}

/* 商家列表 */
.business-list {
  display: flex;
  flex-direction: column;
  gap: 3vw;
}

.business-card {
  background-color: white;
  border-radius: 3vw;
  padding: 4vw;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.business-info {
  flex: 1;
}

.business-name {
  font-size: 4.2vw;
  font-weight: 600;
  color: #333;
}

.business-address {
  font-size: 3.2vw;
  color: #888;
  margin-top: 1vw;
}

.business-meta {
  display: flex;
  gap: 4vw;
  margin-top: 1.5vw;
}

.meta-item {
  font-size: 3vw;
  color: #666;
  display: flex;
  align-items: center;
  gap: 1vw;
}

.meta-item i {
  color: #0097FF;
}

.arrow-icon {
  font-size: 5vw;
  color: #ccc;
}

/* 操作栏 */
.action-bar {
  margin-bottom: 3vw;
}

.action-btn {
  padding: 2vw 4vw;
  border: none;
  border-radius: 2vw;
  font-size: 3.5vw;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 1vw;
}

.add-btn {
  background-color: #0097FF;
  color: white;
}

/* 食品列表 */
.food-list {
  display: flex;
  flex-direction: column;
  gap: 3vw;
}

.food-card {
  background-color: white;
  border-radius: 3vw;
  padding: 4vw;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.food-info {
  margin-bottom: 3vw;
}

.food-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.food-name {
  font-size: 4vw;
  font-weight: 600;
  color: #333;
}

.food-price {
  font-size: 4vw;
  font-weight: 600;
  color: #ff6b6b;
}

.food-desc {
  font-size: 3vw;
  color: #888;
  margin-top: 1vw;
}

.food-actions {
  display: flex;
  gap: 3vw;
  justify-content: flex-end;
}

.edit-btn, .delete-btn {
  padding: 1.5vw 3vw;
  border: none;
  border-radius: 2vw;
  font-size: 3vw;
  cursor: pointer;
}

.edit-btn {
  background-color: #e8f4ff;
  color: #0097FF;
}

.delete-btn {
  background-color: #fff0f0;
  color: #ff6b6b;
}

/* 食品表单 */
.food-form {
  background-color: white;
  border-radius: 3vw;
  padding: 4vw;
}

.form-title {
  font-size: 4vw;
  font-weight: 600;
  color: #333;
  margin-bottom: 4vw;
  text-align: center;
}

.form-item {
  margin-bottom: 3vw;
}

.form-label {
  font-size: 3.2vw;
  color: #666;
  display: block;
  margin-bottom: 1.5vw;
}

.form-input {
  width: 100%;
  height: 10vw;
  padding: 0 3vw;
  border: 1px solid #ddd;
  border-radius: 2vw;
  font-size: 3.5vw;
  box-sizing: border-box;
  outline: none;
}

.form-input-small {
  width: 50%;
}

.form-textarea {
  width: 100%;
  padding: 2vw 3vw;
  border: 1px solid #ddd;
  border-radius: 2vw;
  font-size: 3.5vw;
  box-sizing: border-box;
  outline: none;
  resize: none;
}

.form-buttons {
  display: flex;
  gap: 3vw;
  margin-top: 4vw;
}

.cancel-btn, .submit-btn {
  flex: 1;
  height: 10vw;
  border: none;
  border-radius: 5vw;
  font-size: 3.8vw;
  cursor: pointer;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #666;
}

.submit-btn {
  background-color: #0097FF;
  color: white;
}

/* 操作卡片 */
.action-cards {
  display: flex;
  flex-direction: column;
  gap: 4vw;
}

.action-card {
  background-color: white;
  border-radius: 3vw;
  padding: 5vw;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4vw;
}

.card-icon {
  width: 14vw;
  height: 14vw;
  border-radius: 50%;
  background-color: #e8f4ff;
  color: #0097FF;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 7vw;
  flex-shrink: 0;
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 4.5vw;
  font-weight: 600;
  color: #333;
}

.card-desc {
  font-size: 3.2vw;
  color: #888;
  margin-top: 1vw;
}

.empty-tip {
  text-align: center;
  color: #bbb;
  font-size: 3.5vw;
  padding: 6vw 0;
  background-color: white;
  border-radius: 3vw;
}

/* 响应式 */
@media (min-width: 768px) {
  .wrapper {
    padding-top: 80px;
    padding-bottom: 30px;
  }

  header {
    height: 80px;
    font-size: 24px;
  }

  .back-btn {
    width: 50px;
    height: 50px;
    font-size: 28px;
    top: 15px;
    left: 20px;
  }

  .card-container {
    padding: 20px;
    max-width: 800px;
    margin: 0 auto;
  }

  .section {
    margin-bottom: 25px;
  }

  .section-title {
    font-size: 18px;
  }

  .badge {
    font-size: 14px;
    padding: 2px 10px;
  }

  .business-list {
    gap: 15px;
  }

  .business-card {
    border-radius: 12px;
    padding: 16px;
  }

  .business-name {
    font-size: 18px;
  }

  .business-address {
    font-size: 14px;
  }

  .business-meta {
    gap: 20px;
  }

  .meta-item {
    font-size: 14px;
  }

  .arrow-icon {
    font-size: 24px;
  }

  .action-btn {
    padding: 8px 16px;
    border-radius: 8px;
    font-size: 14px;
  }

  .food-list, .food-form {
    gap: 15px;
  }

  .food-card, .food-form {
    border-radius: 12px;
    padding: 16px;
  }

  .food-name, .food-price {
    font-size: 16px;
  }

  .food-desc {
    font-size: 14px;
  }

  .edit-btn, .delete-btn {
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 14px;
  }

  .form-title {
    font-size: 18px;
    margin-bottom: 20px;
  }

  .form-item {
    margin-bottom: 15px;
  }

  .form-label {
    font-size: 14px;
    margin-bottom: 8px;
  }

  .form-input {
    height: 45px;
    border-radius: 8px;
    font-size: 15px;
  }

  .form-input-small {
    width: 40%;
  }

  .form-textarea {
    border-radius: 8px;
    font-size: 15px;
  }

  .form-buttons {
    margin-top: 20px;
  }

  .cancel-btn, .submit-btn {
    height: 45px;
    border-radius: 22px;
    font-size: 16px;
  }

  .action-cards {
    gap: 20px;
  }

  .action-card {
    border-radius: 12px;
    padding: 20px;
    gap: 20px;
  }

  .card-icon {
    width: 70px;
    height: 70px;
    font-size: 32px;
  }

  .card-title {
    font-size: 20px;
  }

  .card-desc {
    font-size: 14px;
  }

  .empty-tip {
    font-size: 14px;
    padding: 24px 0;
    border-radius: 12px;
  }
}
</style>
