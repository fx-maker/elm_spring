<template>
  <div class="wrapper">
    <!-- 后退按钮 -->
    <div class="back-btn" @click="back">
      <i class="fa fa-arrow-left"></i>
    </div>

    <!-- 页面标题 -->
    <header class="page-header">
      <p>管理食品</p>
    </header>

    <!-- 商家选择区域 -->
    <div class="section" v-if="!selectedBusiness">
      <div class="section-title">
        <i class="fa fa-building"></i> 选择商家
      </div>
      <div class="business-list">
        <div class="business-card" v-for="b in myBusinesses" :key="b.businessId" @click="selectBusiness(b)">
          <div class="business-name">{{ b.businessName }}</div>
          <div class="business-address">{{ b.businessAddress || '暂无地址' }}</div>
        </div>
        <div v-if="myBusinesses.length === 0" class="empty-tip">
          暂无可用商家，请先新增商家或联系管理员分配商家
        </div>
      </div>
    </div>

    <!-- 食品管理区域 -->
    <div class="section" v-else>
      <div class="section-header">
        <div class="section-title">
          <i class="fa fa-cutlery"></i> {{ selectedBusiness.businessName }} 的食品
        </div>
        <span class="change-btn" @click="selectedBusiness = null; showForm = false;">切换商家</span>
      </div>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <button class="action-btn add-btn" @click="showAddForm">
          <i class="fa fa-plus"></i> 新增食品
        </button>
      </div>

      <!-- 食品列表 -->
      <div class="food-list" v-if="!showForm">
        <div class="food-card" v-for="f in foodList" :key="f.foodId">
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
        <div v-if="foodList.length === 0" class="empty-tip">
          暂无食品，点击上方按钮添加
        </div>
      </div>

      <!-- 新增/编辑表单 -->
      <div class="form-container" v-if="showForm">
        <div class="form-title">{{ isEditing ? '编辑食品' : '新增食品' }}</div>

        <div class="form-item">
          <label class="form-label">食品名称 *</label>
          <input
            class="form-input"
            v-model="foodForm.foodName"
            placeholder="请输入食品名称"
            maxlength="50"
          >
        </div>

        <div class="form-item">
          <label class="form-label">食品说明</label>
          <textarea
            class="form-textarea"
            v-model="foodForm.foodExplain"
            placeholder="请简要描述食品特点"
            maxlength="200"
            rows="3"
          ></textarea>
        </div>

        <div class="form-item">
          <label class="form-label">图片地址</label>
          <input
            class="form-input"
            v-model="foodForm.foodImg"
            placeholder="请输入图片URL地址"
          >
        </div>

        <div class="form-item">
          <label class="form-label">食品价格 *</label>
          <input
            class="form-input form-input-small"
            v-model="foodForm.foodPrice"
            placeholder="请输入价格"
            type="number"
            step="0.01"
          >
        </div>

        <div class="form-item">
          <label class="form-label">备注</label>
          <input
            class="form-input"
            v-model="foodForm.remarks"
            placeholder="可选备注信息"
            maxlength="100"
          >
        </div>

        <div class="form-buttons">
          <button class="cancel-btn" @click="cancelForm">取消</button>
          <button class="submit-btn" @click="submitFood">保存</button>
        </div>
      </div>
    </div>

    <Footer class="page-footer" />
  </div>
</template>

<script>
import Footer from "@/components/Footer.vue";
import { getSessionStorage } from "@/common";

export default {
  name: "AddFood",
  components: { Footer },
  data() {
    return {
      user: {},
      myBusinesses: [],
      selectedBusiness: null,
      foodList: [],
      showForm: false,
      isEditing: false,
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
      this.$router.back();
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
      this.showForm = false;
      await this.loadFoodList();
    },
    async loadFoodList() {
      try {
        const res = await this.$axios.get(`FoodController/listFoodByBusinessId/${this.selectedBusiness.businessId}`);
        if (res.data.code === 200) {
          this.foodList = res.data.result || [];
        }
      } catch (e) {
        console.error("加载食品列表失败", e);
      }
    },
    showAddForm() {
      this.isEditing = false;
      this.foodForm = {
        foodId: null,
        foodName: "",
        foodExplain: "",
        foodImg: "",
        foodPrice: "",
        remarks: ""
      };
      this.showForm = true;
    },
    editFood(food) {
      this.isEditing = true;
      this.foodForm = {
        foodId: food.foodId,
        foodName: food.foodName,
        foodExplain: food.foodExplain || "",
        foodImg: food.foodImg || "",
        foodPrice: food.foodPrice,
        remarks: food.remarks || ""
      };
      this.showForm = true;
    },
    cancelForm() {
      this.showForm = false;
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
        if (this.isEditing) {
          // 更新食品
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
          // 新增食品
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
          alert(this.isEditing ? "修改成功！" : "添加成功！");
          this.showForm = false;
          await this.loadFoodList();
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
          await this.loadFoodList();
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
  padding-bottom: 18vw;
  font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

/* 后退按钮 */
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

/* 页面标题 */
.page-header {
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

/* 内容区域 */
.section {
  padding: 4vw;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 3vw;
}

.section-title {
  font-size: 4vw;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 2vw;
}

.section-title i {
  color: #0097FF;
}

.change-btn {
  font-size: 3vw;
  color: #0097FF;
  cursor: pointer;
}

/* 商家列表 */
.business-list {
  display: flex;
  flex-direction: column;
  gap: 3vw;
}

.business-card {
  background-color: #fff;
  border-radius: 3vw;
  padding: 4vw;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
}

.business-name {
  font-size: 4vw;
  font-weight: 600;
  color: #333;
  margin-bottom: 1vw;
}

.business-address {
  font-size: 3vw;
  color: #888;
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
  background-color: #fff;
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
  margin-bottom: 1.5vw;
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

/* 表单 */
.form-container {
  background-color: #fff;
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
  margin-bottom: 4vw;
}

.form-label {
  font-size: 3.5vw;
  color: #666;
  display: block;
  margin-bottom: 2vw;
}

.form-input {
  width: 100%;
  height: 11vw;
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
  padding: 3vw;
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
  margin-top: 5vw;
}

.cancel-btn, .submit-btn {
  flex: 1;
  height: 12vw;
  border: none;
  border-radius: 6vw;
  font-size: 4vw;
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

/* 空提示 */
.empty-tip {
  text-align: center;
  color: #bbb;
  font-size: 3.5vw;
  padding: 6vw 0;
}

/* 底部 */
.page-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 999;
}

/* 响应式 */
@media (min-width: 768px) {
  .wrapper {
    padding-top: 80px;
    padding-bottom: 90px;
  }

  .back-btn {
    width: 50px;
    height: 50px;
    font-size: 28px;
    top: 15px;
    left: 20px;
  }

  .page-header {
    height: 80px;
    font-size: 24px;
  }

  .section {
    padding: 20px;
    max-width: 600px;
    margin: 0 auto;
  }

  .section-header {
    margin-bottom: 15px;
  }

  .section-title {
    font-size: 18px;
  }

  .change-btn {
    font-size: 14px;
  }

  .business-list {
    gap: 15px;
  }

  .business-card {
    border-radius: 12px;
    padding: 16px;
  }

  .business-name {
    font-size: 16px;
  }

  .business-address {
    font-size: 14px;
  }

  .action-btn {
    padding: 8px 16px;
    border-radius: 8px;
    font-size: 14px;
  }

  .food-list {
    gap: 15px;
  }

  .food-card {
    border-radius: 12px;
    padding: 16px;
  }

  .food-name {
    font-size: 16px;
  }

  .food-price {
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

  .form-container {
    border-radius: 12px;
    padding: 20px;
  }

  .form-title {
    font-size: 18px;
    margin-bottom: 20px;
  }

  .form-item {
    margin-bottom: 20px;
  }

  .form-label {
    font-size: 14px;
    margin-bottom: 8px;
  }

  .form-input {
    height: 50px;
    border-radius: 8px;
    font-size: 16px;
  }

  .form-input-small {
    width: 40%;
  }

  .form-textarea {
    border-radius: 8px;
    font-size: 16px;
  }

  .form-buttons {
    margin-top: 25px;
  }

  .cancel-btn, .submit-btn {
    height: 50px;
    border-radius: 25px;
    font-size: 16px;
  }

  .empty-tip {
    font-size: 14px;
  }
}
</style>
