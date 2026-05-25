<template>
  <div class="wrapper">
    <!-- 头部：身份不同标题不同 -->
    <header class="page-header">
      <span class="header-title">{{ pageTitle }}</span>
    </header>

    <!-- 用户信息（通用，所有身份都有） -->
    <div class="info-box username-box">
      <span class="box-label">用户名：</span>
      <span class="box-content">{{ user.userName || '未设置用户名' }}</span>
    </div>
    <div class="info-box phone-box">
      <span class="box-label">注册电话：</span>
      <span class="box-content">{{ user.userId || '未绑定手机号' }}</span>
    </div>

    <!-- 普通用户入口：钱包 + 积分 -->
    <template v-if="!user.userType || user.userType === 'USER'">
      <div class="info-box wallet-box" @click="$router.push('/walletPage')">
        <span class="box-label"><i class="fa fa-credit-card"></i> 我的钱包</span>
        <i class="fa fa-angle-right"></i>
      </div>
      <div class="info-box points-box" @click="$router.push('/pointsPage')">
        <span class="box-label"><i class="fa fa-gift"></i> 我的积分</span>
        <i class="fa fa-angle-right"></i>
      </div>
    </template>

    <!-- 商家用户入口：商家管理 -->
    <template v-else-if="user.userType === 'BUSINESS'">
      <div class="info-box business-box" @click="$router.push('/addBusinessPage')">
        <span class="box-label"><i class="fa fa-building"></i> 商家管理</span>
        <i class="fa fa-angle-right"></i>
      </div>
    </template>

    <!-- 管理员入口：系统管理 -->
    <template v-else-if="user.userType === 'ADMIN'">
      <div class="info-box admin-box" @click="$router.push('/adminPage')">
        <span class="box-label"><i class="fa fa-cogs"></i> 系统管理</span>
        <i class="fa fa-angle-right"></i>
      </div>
    </template>

    <!-- 退出登录（所有身份都有） -->
    <div class="info-box logout-box" @click="logout">
      <span class="box-label"><i class="fa fa-sign-out"></i> 退出登录</span>
      <i class="fa fa-angle-right"></i>
    </div>

    <Footer class="page-footer" />
  </div>
</template>

<script>
import Footer from "@/components/Footer.vue";
import { getSessionStorage, removeSessionStorage } from "@/common";

export default {
  name: "MyPage",
  components: { Footer },
  data() {
    const rawUser = getSessionStorage('user');
    const user = typeof rawUser === 'string' ? JSON.parse(rawUser) : (rawUser || {});
    return {
      user,
      pageTitle:
        user.userType === 'ADMIN'    ? '系统管理' :
        user.userType === 'BUSINESS' ? '商家管理' : '我的信息'
    };
  },
  methods: {
    logout() {
      if (confirm("确定退出登录吗？")) {
        removeSessionStorage("auth");
        removeSessionStorage("user");
        sessionStorage.removeItem("auth");
        sessionStorage.removeItem("user");
        this.$router.push({ path: "/login" });
      }
    }
  }
};
</script>

<style scoped>
/* 基础容器：控制整体间距与排列 */
.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-top: 12vw; /* 头部空间 */
  padding-bottom: 15vw; /* 底部空间 */
  padding-left: 4vw;
  padding-right: 4vw;
  box-sizing: border-box;
  font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  /* 垂直排列两个框，控制间距 */
  display: flex;
  flex-direction: column;
  gap: 3vw; /* 两个框之间的间距 */
}

/* 头部：居中显示标题，保持视觉统一 */
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
  padding: 0 4vw;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 通用信息框样式：两个框保持统一视觉风格 */
.info-box {
  width: 100%;
  height: 15vw;
  background-color: #ffffff;
  border-radius: 4vw;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center; /* 文字垂直居中 */
  padding-left: 6vw; /* 左侧内边距，避免文字贴边 */
  box-sizing: border-box;
}

/* 框内标签样式：次要信息，浅色调区分 */
.box-label {
  font-size: 3.8vw;
  color: #666666;
  font-weight: 500;
  margin-right: 2vw; /* 与内容保持间距 */
}

/* 框内内容样式：统一突出显示，与头部色调呼应 */
.box-content {
  font-size: 4.2vw;
  color: #0097FF; /* 主色调呼应头部，增强整体感 */
  font-weight: 600;
  /* 处理过长内容：换行显示，限制宽度 */
  white-space: normal;
  line-height: 1.4;
  max-width: 60%;
}

/* 钱包、积分、商家、管理员入口通用样式 */
.wallet-box, .points-box, .business-box, .admin-box {
  cursor: pointer;
  justify-content: space-between;
  padding-right: 6vw;
}

.wallet-box .box-label i, .points-box .box-label i,
.business-box .box-label i, .admin-box .box-label i {
  margin-right: 2vw;
  color: #0097FF;
}

.wallet-box .box-label, .points-box .box-label,
.business-box .box-label, .admin-box .box-label {
  display: flex;
  align-items: center;
  font-size: 4vw;
  color: #333;
  max-width: none;
}

.wallet-box i.fa-angle-right, .points-box i.fa-angle-right,
.business-box i.fa-angle-right, .admin-box i.fa-angle-right, .logout-box i.fa-angle-right {
  font-size: 6vw;
  color: #ccc;
}

/* 退出登录按钮：红色警示风格 */
.logout-box {
  cursor: pointer;
  justify-content: space-between;
  padding-right: 6vw;
}

.logout-box .box-label {
  display: flex;
  align-items: center;
  font-size: 4vw;
  color: #FF4757;
  max-width: none;
}

.logout-box .box-label i {
  margin-right: 2vw;
  color: #FF4757;
}

/* 底部组件：固定定位，不遮挡内容 */
.page-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 999;
}

/* 响应式适配（平板及以上屏幕） */
@media (min-width: 768px) {
  .wrapper {
    padding-top: 80px;
    padding-bottom: 80px;
    padding-left: 20px;
    padding-right: 20px;
    max-width: 600px;
    margin: 0 auto; /* 页面整体居中 */
    gap: 20px; /* 大屏下两个框间距调整为20px */
  }

  .page-header {
    height: 80px;
    font-size: 24px;
    padding: 0 20px;
  }

  .info-box {
    height: 70px;
    border-radius: 20px;
    padding-left: 30px; /* 大屏左侧内边距调整 */
  }

  .box-label {
    font-size: 16px;
    margin-right: 10px;
  }

  .box-content {
    font-size: 18px;
    max-width: 70%;
  }
}
</style>