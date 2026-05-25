<template>
	<div class="wrapper">
		<header>
			<p>我的钱包</p>
		</header>

		<!-- 钱包余额卡片 -->
		<div class="balance-card">
			<div class="balance-label">账户余额（元）</div>
			<div class="balance-amount">{{wallet.money || 0}}</div>
			<div class="balance-frozen" v-if="wallet.frozenMoney > 0">
				冻结：{{wallet.frozenMoney}}元
			</div>
		</div>

		<!-- VIP状态 -->
		<div class="vip-card">
			<div class="vip-left">
				<span class="vip-tag" :class="{active: wallet.isVip === 1}">
					{{wallet.isVip === 1 ? 'VIP会员' : '普通用户'}}
				</span>
				<span class="vip-limit" v-if="wallet.isVip === 1">
					透支额度：{{wallet.overdraftLimit}}元（已用：{{wallet.overdraftAmount}}元）
				</span>
			</div>
		</div>

		<!-- 操作按钮 -->
		<div class="action-buttons">
			<div class="btn-recharge" @click="showRecharge = true">充值</div>
			<div class="btn-vip" @click="showVip = true">开通VIP</div>
		</div>

		<!-- 交易记录入口 -->
		<div class="transaction-entry" @click="showTransaction = true">
			<span>交易记录</span>
			<i class="fa fa-angle-right"></i>
		</div>

		<!-- 充值弹窗 -->
		<div class="modal" v-if="showRecharge" @click.self="showRecharge = false">
			<div class="modal-content">
				<h4>充值金额</h4>
				<input type="number" v-model="rechargeAmount" placeholder="请输入充值金额">
				<div class="modal-buttons">
					<button @click="showRecharge = false">取消</button>
					<button class="confirm" @click="handleRecharge">确认充值</button>
				</div>
			</div>
		</div>

		<!-- VIP弹窗 -->
		<div class="modal" v-if="showVip" @click.self="showVip = false">
			<div class="modal-content">
				<h4>开通VIP会员</h4>
				<p>VIP会员可享受透支服务</p>
				<input type="number" v-model="overdraftLimit" placeholder="设置透支额度">
				<div class="modal-buttons">
					<button @click="showVip = false">取消</button>
					<button class="confirm" @click="handleVip">确认开通</button>
				</div>
			</div>
		</div>

		<!-- 交易记录弹窗 -->
		<div class="modal full-modal" v-if="showTransaction" @click.self="showTransaction = false">
			<div class="modal-content">
				<h4>交易记录</h4>
				<div class="transaction-list">
					<div class="transaction-item" v-for="tx in transactions" :key="tx.transactionId">
						<div class="tx-info">
							<span class="tx-type">{{getTypeName(tx.type)}}</span>
							<span class="tx-desc">{{tx.description}}</span>
						</div>
						<div class="tx-amount" :class="{plus: tx.type === 0 || tx.type === 4, minus: tx.type === 2}">
							{{tx.type === 0 || tx.type === 4 ? '+' : '-'}}{{tx.money}}
						</div>
					</div>
					<div class="empty" v-if="transactions.length === 0">暂无交易记录</div>
				</div>
				<button class="confirm" @click="showTransaction = false">关闭</button>
			</div>
		</div>

		<Footer class="page-footer" />
	</div>
</template>

<script>
import Footer from "@/components/Footer.vue";

export default {
	name: "WalletPage",
	components: { Footer },
	data() {
		return {
			wallet: {},
			transactions: [],
			showRecharge: false,
			showVip: false,
			showTransaction: false,
			rechargeAmount: '',
			overdraftLimit: ''
		};
	},
	created() {
		const user = this.$getSessionStorage('user');
		if (!user) {
			this.$router.push('/login');
			return;
		}
		this.loadWallet(user.userId);
		this.loadTransactions(user.userId);
	},
	methods: {
		loadWallet(userId) {
			this.$axios.get(`WalletController/getWallet/${userId}`).then(response => {
				if (response.data.code === 200) {
					this.wallet = response.data.result;
				}
			}).catch(error => {
				console.error(error);
			});
		},
		loadTransactions(userId) {
			this.$axios.get(`WalletController/listTransaction/${userId}`).then(response => {
				if (response.data.code === 200) {
					this.transactions = response.data.result || [];
				}
			}).catch(error => {
				console.error(error);
			});
		},
		handleRecharge() {
			if (!this.rechargeAmount || this.rechargeAmount <= 0) {
				alert('请输入正确的充值金额');
				return;
			}
			const user = this.$getSessionStorage('user');
			this.$axios.post(`WalletController/recharge/${user.userId}/${this.rechargeAmount}`).then(response => {
				if (response.data.code === 200) {
					alert('充值成功');
					this.showRecharge = false;
					this.rechargeAmount = '';
					this.loadWallet(user.userId);
					this.loadTransactions(user.userId);
				}
			}).catch(() => {
				alert('充值失败');
			});
		},
		handleVip() {
			if (!this.overdraftLimit || this.overdraftLimit <= 0) {
				alert('请输入透支额度');
				return;
			}
			const user = this.$getSessionStorage('user');
			this.$axios.post(`WalletController/setVipStatus/${user.userId}/1/${this.overdraftLimit}`).then(response => {
				if (response.data.code === 200) {
					alert('VIP开通成功');
					this.showVip = false;
					this.overdraftLimit = '';
					this.loadWallet(user.userId);
				}
			}).catch(() => {
				alert('开通失败');
			});
		},
		getTypeName(type) {
			const types = ['充值', '提现', '支付', '冻结', '解冻', '透支', '还款', '利息'];
			return types[type] || '未知';
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

.balance-card {
	margin-top: 16vw;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	border-radius: 4vw;
	padding: 6vw;
	color: #fff;
	text-align: center;
}

.balance-label {
	font-size: 3.5vw;
	opacity: 0.9;
}

.balance-amount {
	font-size: 10vw;
	font-weight: 700;
	margin: 2vw 0;
}

.balance-frozen {
	font-size: 3vw;
	opacity: 0.8;
}

.vip-card {
	margin-top: 3vw;
	background: #fff;
	border-radius: 4vw;
	padding: 4vw;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.vip-tag {
	display: inline-block;
	padding: 1vw 3vw;
	border-radius: 2vw;
	background: #ccc;
	color: #fff;
	font-size: 3.5vw;
}

.vip-tag.active {
	background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.vip-limit {
	font-size: 3vw;
	color: #999;
	margin-left: 3vw;
}

.action-buttons {
	display: flex;
	margin-top: 3vw;
	gap: 3vw;
}

.btn-recharge, .btn-vip {
	flex: 1;
	height: 10vw;
	border-radius: 4vw;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 4vw;
	font-weight: 700;
	color: #fff;
	cursor: pointer;
}

.btn-recharge {
	background: linear-gradient(135deg, #38CA73 0%, #2BA35F 100%);
}

.btn-vip {
	background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.transaction-entry {
	margin-top: 3vw;
	background: #fff;
	border-radius: 4vw;
	padding: 4vw;
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 4vw;
	color: #333;
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
	width: 80%;
	max-width: 400px;
}

.modal-content h4 {
	font-size: 5vw;
	margin-bottom: 3vw;
	text-align: center;
}

.modal-content p {
	font-size: 3.5vw;
	color: #999;
	text-align: center;
	margin-bottom: 3vw;
}

.modal-content input {
	width: 100%;
	height: 10vw;
	border: 1px solid #ddd;
	border-radius: 2vw;
	padding: 0 3vw;
	font-size: 4vw;
	box-sizing: border-box;
	margin-bottom: 3vw;
}

.modal-buttons {
	display: flex;
	gap: 3vw;
}

.modal-buttons button {
	flex: 1;
	height: 10vw;
	border: none;
	border-radius: 2vw;
	font-size: 4vw;
	cursor: pointer;
	background: #eee;
	color: #666;
}

.modal-buttons button.confirm {
	background: #0097FF;
	color: #fff;
}

.full-modal .modal-content {
	width: 90%;
	height: 70vh;
	display: flex;
	flex-direction: column;
}

.transaction-list {
	flex: 1;
	overflow-y: auto;
	margin: 3vw 0;
}

.transaction-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 3vw 0;
	border-bottom: 1px solid #f0f0f0;
}

.tx-info {
	display: flex;
	flex-direction: column;
}

.tx-type {
	font-size: 4vw;
	color: #333;
}

.tx-desc {
	font-size: 3vw;
	color: #999;
	margin-top: 1vw;
}

.tx-amount {
	font-size: 4.5vw;
	font-weight: 700;
}

.tx-amount.plus { color: #38CA73; }
.tx-amount.minus { color: #f5576c; }

.empty {
	text-align: center;
	color: #999;
	font-size: 3.5vw;
	padding: 10vw 0;
}

.page-footer {
	position: fixed;
	bottom: 0;
	left: 0;
	width: 100%;
	z-index: 999;
}
</style>
