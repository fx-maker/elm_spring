<template>
	<div class="wrapper">
		<header>
			<p>我的积分</p>
		</header>

		<!-- 积分余额卡片 -->
		<div class="points-card">
			<div class="points-label">我的积分</div>
			<div class="points-amount">{{points.availablePoints || 0}}</div>
			<div class="points-total">历史总积分：{{points.totalPoints || 0}}</div>
		</div>

		<!-- 操作区域 -->
		<div class="action-area">
			<div class="btn-detail" @click="showDetail = true">积分明细</div>
		</div>

		<!-- 积分明细弹窗 -->
		<div class="modal full-modal" v-if="showDetail" @click.self="showDetail = false">
			<div class="modal-content">
				<h4>积分明细</h4>
				<div class="detail-list">
					<div class="detail-item" v-for="item in details" :key="item.detailId">
						<div class="detail-info">
							<span class="detail-type">{{getTypeName(item.type)}}</span>
							<span class="detail-desc">{{item.description}}</span>
							<span class="detail-time">{{item.createTime}}</span>
						</div>
						<div class="detail-amount" :class="{plus: item.points > 0}">
							{{item.points > 0 ? '+' : ''}}{{item.points}}
						</div>
					</div>
					<div class="empty" v-if="details.length === 0">暂无积分记录</div>
				</div>
				<button class="confirm" @click="showDetail = false">关闭</button>
			</div>
		</div>

		<Footer class="page-footer" />
	</div>
</template>

<script>
import Footer from "@/components/Footer.vue";

export default {
	name: "PointsPage",
	components: { Footer },
	data() {
		return {
			points: {},
			details: [],
			showDetail: false
		};
	},
	created() {
		const user = this.$getSessionStorage('user');
		if (!user) {
			this.$router.push('/login');
			return;
		}
		this.loadPoints(user.userId);
		this.loadDetails(user.userId);
	},
	methods: {
		loadPoints(userId) {
			this.$axios.get(`PointsController/getPoints/${userId}`).then(response => {
				if (response.data.code === 200) {
					this.points = response.data.result;
				}
			}).catch(error => {
				console.error(error);
			});
		},
		loadDetails(userId) {
			this.$axios.get(`PointsController/listPointsDetail/${userId}`).then(response => {
				if (response.data.code === 200) {
					this.details = response.data.result || [];
				}
			}).catch(error => {
				console.error(error);
			});
		},
		getTypeName(type) {
			const types = {
				'ORDER': '订单获得',
				'COMMENT': '评论获得',
				'ACTIVITY': '活动获得',
				'EXCHANGE': '积分兑换',
				'DEDUCTION': '订单抵扣',
				'EXPIRE': '过期扣减'
			};
			return types[type] || type || '未知';
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
	padding-bottom: 15vw;
	padding-left: 4vw;
	padding-right: 4vw;
	box-sizing: border-box;
}

.wrapper header {
	width: 100%;
	height: 12vw;
	background-color: #0097FF;
	color: #fff;
	font-size: 4.8vw;
	font-weight: 500;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 1000;
	display: flex;
	justify-content: center;
	align-items: center;
}

.points-card {
	margin-top: 16vw;
	background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
	border-radius: 4vw;
	padding: 6vw;
	color: #fff;
	text-align: center;
}

.points-label {
	font-size: 3.5vw;
	opacity: 0.9;
}

.points-amount {
	font-size: 12vw;
	font-weight: 700;
	margin: 2vw 0;
}

.points-total {
	font-size: 3.5vw;
	opacity: 0.8;
}

.action-area {
	margin-top: 3vw;
}

.btn-detail {
	height: 10vw;
	background: #fff;
	border-radius: 4vw;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 4vw;
	font-weight: 700;
	color: #11998e;
	cursor: pointer;
}

.modal {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
	z-index: 2000;
	display: flex;
	justify-content: center;
	align-items: center;
}

.modal-content {
	background: #fff;
	border-radius: 4vw;
	padding: 6vw;
	width: 90%;
	height: 70vh;
	display: flex;
	flex-direction: column;
}

.modal-content h4 {
	font-size: 5vw;
	margin-bottom: 3vw;
	text-align: center;
}

.detail-list {
	flex: 1;
	overflow-y: auto;
}

.detail-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 3vw 0;
	border-bottom: 1px solid #f0f0f0;
}

.detail-info {
	display: flex;
	flex-direction: column;
}

.detail-type {
	font-size: 4vw;
	color: #333;
}

.detail-desc {
	font-size: 3vw;
	color: #999;
	margin-top: 1vw;
}

.detail-time {
	font-size: 2.5vw;
	color: #bbb;
	margin-top: 1vw;
}

.detail-amount {
	font-size: 4.5vw;
	font-weight: 700;
	color: #f5576c;
}

.detail-amount.plus {
	color: #38CA73;
}

.empty {
	text-align: center;
	color: #999;
	font-size: 3.5vw;
	padding: 10vw 0;
}

button.confirm {
	height: 10vw;
	border: none;
	border-radius: 2vw;
	font-size: 4vw;
	cursor: pointer;
	background: #11998e;
	color: #fff;
	margin-top: 3vw;
}

.page-footer {
	position: fixed;
	bottom: 0;
	left: 0;
	width: 100%;
	z-index: 999;
}
</style>
