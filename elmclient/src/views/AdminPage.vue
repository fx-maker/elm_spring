<template>
  <div class="wrapper">
    <!-- 后退按钮 -->
    <div class="back-btn" @click="$router.back()">
      <i class="fa fa-arrow-left"></i>
    </div>

    <header class="page-header">
      <span>系统管理</span>
    </header>

    <!-- 用户列表 -->
    <section class="section">
      <div class="section-title">
        <i class="fa fa-users"></i> 用户列表
        <span class="badge">{{ users.length }}</span>
      </div>
      <div class="card" v-for="u in users" :key="u.userId">
        <div class="card-row">
          <span class="label">账号</span>
          <span class="value">{{ u.userId }}</span>
        </div>
        <div class="card-row">
          <span class="label">姓名</span>
          <span class="value">{{ u.userName }}</span>
        </div>
        <div class="card-row">
          <span class="label">身份</span>
          <span class="value type-badge" :class="u.userType">{{ typeName(u.userType) }}</span>
        </div>
        <div class="card-row" v-if="u.userType !== 'ADMIN'">
          <span class="label">操作</span>
          <span class="value">
            <select class="type-select" @change="changeUserType(u.userId, $event)">
              <option value="">-- 更改身份 --</option>
              <option v-if="u.userType === 'USER'" value="BUSINESS">升级为商家</option>
              <option v-if="u.userType === 'BUSINESS'" value="USER">降级为用户</option>
              <option v-if="u.userType === 'USER'" value="ADMIN">升级为管理员</option>
            </select>
          </span>
        </div>
      </div>
    </section>

    <!-- 商家列表 -->
    <section class="section">
      <div class="section-title">
        <i class="fa fa-building"></i> 商家列表
        <span class="badge">{{ businesses.length }}</span>
      </div>
      <div class="card" v-for="b in businesses" :key="b.businessId">
        <div class="card-row">
          <span class="label">ID</span>
          <span class="value">{{ b.businessId }}</span>
        </div>
        <div class="card-row">
          <span class="label">商家名</span>
          <span class="value">{{ b.businessName }}</span>
        </div>
        <div class="card-row">
          <span class="label">地址</span>
          <span class="value">{{ b.businessAddress }}</span>
        </div>
        <div class="card-row">
          <span class="label">起送价</span>
          <span class="value">￥{{ b.starPrice }}</span>
        </div>
        <div class="card-row">
          <span class="label">商家用户</span>
          <span class="value">
            <template v-if="b.ownerId">
              {{ ownerName(b.ownerId) }}
            </template>
            <template v-else>
              <select class="owner-select" @change="assignOwner(b.businessId, $event)">
                <option value="">-- 分配用户 --</option>
                <option v-for="u in businessUsers" :key="u.userId" :value="u.userId">
                  {{ u.userName }} ({{ u.userId }})
                </option>
              </select>
            </template>
          </span>
        </div>
      </div>
      <div v-if="businesses.length === 0" class="empty-tip">暂无商家</div>
    </section>

    <Footer class="page-footer" />
  </div>
</template>

<script>
import Footer from "@/components/Footer.vue";

export default {
  name: "AdminPage",
  components: { Footer },
  data() {
    return {
      users: [],
      businesses: [],
      businessUsers: []   // userType=BUSINESS 的用户
    };
  },
  mounted() {
    this.loadUsers();
    this.loadBusinesses();
  },
  methods: {
    typeName(type) {
      return type === 'ADMIN' ? '管理员' : type === 'BUSINESS' ? '商家' : '普通用户';
    },
    ownerName(userId) {
      const u = this.businessUsers.find(x => x.userId === userId);
      return u ? `${u.userName} (${u.userId})` : userId;
    },
    async loadUsers() {
      try {
        const res = await this.$axios.get("UserController/listAllUsers");
        if (res.data.code === 200) {
          this.users = res.data.result || [];
          this.businessUsers = this.users.filter(u => u.userType === 'BUSINESS');
        }
      } catch (e) {
        console.error("加载用户列表失败", e);
      }
    },
    async loadBusinesses() {
      try {
        const res = await this.$axios.get("BusinessController/listBusinessByOrderTypeId/1");
        if (res.data.code === 200) this.businesses = res.data.result || [];
      } catch (e) {
        console.error("加载商家列表失败", e);
      }
    },
    async changeUserType(userId, event) {
      const userType = event.target.value;
      if (!userType) return;
      if (!confirm(`确定要将该用户身份更改为"${this.typeName(userType)}"吗？`)) {
        event.target.value = "";
        return;
      }
      try {
        const res = await this.$axios.put(`UserController/updateUserType/${userId}/${userType}`);
        if (res.data.code === 200) {
          alert("修改成功");
          this.loadUsers();
        } else {
          alert("修改失败");
        }
      } catch (e) {
        alert("修改失败");
        console.error(e);
      }
      event.target.value = "";
    },
    async assignOwner(businessId, event) {
      const ownerId = event.target.value;
      if (!ownerId) return;
      try {
        const res = await this.$axios.put(`BusinessController/updateOwner/${businessId}/${ownerId}`);
        if (res.data.code === 200) {
          this.loadBusinesses();
        } else {
          alert("分配失败");
        }
      } catch (e) {
        alert("分配失败");
        console.error(e);
      }
    }
  }
};
</script>

<style scoped>
.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-top: 12vw;
  padding-bottom: 18vw;
  box-sizing: border-box;
  font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
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

.page-header {
  width: 100%;
  height: 12vw;
  background-color: #0097FF;
  color: #ffffff;
  font-size: 4.8vw;
  font-weight: 500;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section {
  padding: 4vw;
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
  padding: 0.5vw 2.5vw;
  font-size: 3vw;
  font-weight: normal;
}

.card {
  background-color: #fff;
  border-radius: 3vw;
  padding: 4vw;
  margin-bottom: 3vw;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.card-row {
  display: flex;
  align-items: center;
  padding: 1.5vw 0;
  border-bottom: 1px solid #f0f0f0;
}

.card-row:last-child {
  border-bottom: none;
}

.label {
  font-size: 3.5vw;
  color: #999;
  width: 16vw;
  flex-shrink: 0;
}

.value {
  font-size: 3.8vw;
  color: #333;
}

.type-badge {
  padding: 0.5vw 2vw;
  border-radius: 2vw;
  font-size: 3vw;
  font-weight: 500;
}

.type-badge.ADMIN { background: #fff3e0; color: #f57c00; }
.type-badge.BUSINESS { background: #e3f2fd; color: #1976d2; }
.type-badge.USER { background: #e8f5e9; color: #388e3c; }

.empty-tip {
  text-align: center;
  color: #bbb;
  font-size: 3.5vw;
  padding: 6vw 0;
}

.owner-select {
  border: 1px solid #ddd;
  border-radius: 2vw;
  padding: 1vw 2vw;
  font-size: 3.2vw;
  color: #333;
  background: #f9f9f9;
}

.type-select {
  border: 1px solid #ddd;
  border-radius: 2vw;
  padding: 1vw 2vw;
  font-size: 3.2vw;
  color: #333;
  background: #fff3e0;
}

.page-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 999;
}

@media (min-width: 768px) {
  .wrapper { padding-top: 80px; padding-bottom: 90px; }
  .back-btn { width: 50px; height: 50px; font-size: 28px; top: 15px; left: 20px; }
  .page-header { height: 80px; font-size: 24px; }
  .section { padding: 20px; }
  .section-title { font-size: 18px; margin-bottom: 15px; }
  .badge { font-size: 14px; padding: 2px 12px; }
  .card { border-radius: 12px; padding: 16px; margin-bottom: 12px; }
  .card-row { padding: 6px 0; }
  .label { font-size: 14px; width: 70px; }
  .value { font-size: 15px; }
  .type-badge { font-size: 13px; padding: 2px 10px; border-radius: 8px; }
  .empty-tip { font-size: 14px; padding: 24px 0; }
}
</style>
