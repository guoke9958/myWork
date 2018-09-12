package com.cn.xa.qyw.ui.news.fragment;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.cn.xa.qyw.R;
import com.cn.xa.qyw.view.PagerSlidingTabStrip;

public class  QuickContactFragment extends DialogFragment {

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private ContactPagerAdapter adapter;

	public static QuickContactFragment newInstance() {
		QuickContactFragment f = new QuickContactFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		}

		View root = inflater.inflate(R.layout.fragment_quick_contact, container, false);

		tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
		pager = (ViewPager) root.findViewById(R.id.pager);
		adapter = new ContactPagerAdapter();

		pager.setAdapter(adapter);

		tabs.setViewPager(pager);

		return root;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();

		// change dialog width
		if (getDialog() != null) {

			int fullWidth = getDialog().getWindow().getAttributes().width;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				fullWidth = size.x;
			} else {
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				fullWidth = display.getWidth();
			}

			final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
					.getDisplayMetrics());

			int w = fullWidth - padding;
			int h = getDialog().getWindow().getAttributes().height;

			getDialog().getWindow().setLayout(w, h);
		}
	}

	public class ContactPagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

		private final int[] ICONS = { R.drawable.ic_launcher_gplus, R.drawable.ic_launcher_gmail,
				R.drawable.ic_launcher_gmaps, R.drawable.ic_launcher_chrome ,
				R.drawable.ic_launcher_gplus,R.drawable.ic_launcher_gmaps,
				R.drawable.ic_launcher_gmail};

		public ContactPagerAdapter() {
			super();
		}

		@Override
		public int getCount() {
			return ICONS.length;
		}

		@Override
		public int getPageIconResId(int position) {
			return ICONS[position];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// looks a little bit messy here
			TextView v = new TextView(getActivity());
			v.setBackgroundResource(R.color.background_window);
			switch (position){
				case 0:
					v.setText("富迪肽" +"\n"+
							"富迪肽打破传统肽的弊端，满足了人们对“肽”的期望，小分子、酸稳定、强溶解、易吸收、够安全是大豆低聚肽的五大特性，而富迪肽在此基础上分子更小。");
					break;
				case 1:
					v.setText("技术介绍：" + "\n"+
							"肽是被发现的，不是被发明的，肽是天然提取物，本身就存在于自然界物质中。肽是介于氨基酸与蛋白质之间一种生化物质，它比蛋白质分子量小，比氨基酸分子量大，是一个蛋白质的片段。一个氨基酸不能称为肽，必须是两个以上的氨基酸以肽链相连的化合物才能称为肽；许多氨基酸混合在一起也不能称为肽；氨基酸之间必须以肽键相连，形成“氨基酸链”、“氨基酸串”，串起来的氨基酸才能称为肽。两个以上的氨基酸之间以肽键相连，形成的“氨基酸链”或“氨基酸串”才叫做肽。其中，10个以上氨基酸组成的肽被称为多肽，而由2至9个氨基酸组成的叫做寡肽，而只有由2至4个氨基酸组成的才能叫做小分子肽或小肽。\n" +
							"\n" +
							"所以，第一，不要把氨基酸和肽混为一谈，进入人体的氨基酸利用率低于30%，而通过人体器官进入人体本身就存在巨大的消耗和折损，这也就是为何市面上的氨基酸产品明明成分很高但功效相当有限的原因；\n" +
							"\n" +
							"第二，人体是由4万至6万兆个细胞构成，细胞吸收营养的过程都是物质分子穿过细胞膜进入细胞内，葡萄糖和氨基酸等营养成分是以主动运转（主动运输）方式进行吸收，是一种需要消耗细胞代谢能量的方式，人体是无法直接吸收大分子物质的，像蛋白质、多肽等大分子营养物质必须分解转化为小分子物质才能被细胞吸收，当细胞能量不足或病变后由于无法提供充足能量转化就无法吸收营养，细胞缺乏营养能量更加不足，进而加速细胞死亡或者病变。这也就是为何当人体处于亚健康或者疾病状态下，你补充含大量蛋白质、多肽等等营养品短期内效果并不显著的主要原因。\n" +
							"\n" +
							"第三，能被细胞直接吸收的只有小分子物质，细胞几乎不需要消耗能量就可以直接补充小分子物质。小分子活性肽可以直接被细胞吸收，而活性肽又是构建细胞的重要物质，因此人体细胞对于小分子活性肽可以在2分钟内完成吸收转化，10分钟内激活细胞活性，小分子活性肽在人体内的有效利用率高达95%！1958年，美国加利福尼亚大学教授美籍犹太人生物化学家赫伯特·博意尔博士因为发现了活性肽以及活性肽的作用，于当年获得诺贝尔生物学奖，这是关于活性肽的第一个诺贝尔奖。至今为止，全世界已有10位科学家因为对活性肽的研究获得了诺贝尔奖。科学家们研究发现，人体中的每一个细胞和所有重要组成部分都有蛋白质参与。人体内蛋白质的种类、性质、功能各异，核酸、核苷酸。也是蛋白质的一种，而人体中的酶类几乎都是蛋白质组成。可以说蛋白质合成的数量、质量和速度决定着人体的健康状况，当蛋白质合成异常时疾病随之发生。\n" +
							"\n" +
							"如果说蛋白质是构成细胞的重要材料，那么肽就是组成蛋白质的基本材料，科学家们在实验中发现，活性肽就像一个自动运作的监视器，密切监视细胞的表达、复制过程。当细胞分裂、复制正常时，活性肽就保证细胞的正常分裂和蛋白质的正常合成。当细胞分裂出现错误时，活性肽就立即命令错误细胞的复制停下来并对它进行修复。活性肽能及时剪切、剪接和修复异常错误细胞，保证蛋白质的正常合成，保证人体处于健康状态");
					break;
				case 2:
					v.setText("产品介绍：" + "\n"+
							"2017年6月，中华人民共和国国家发展和改革委员会下发了《国家发展改革委 工业和信息化部关于促进食品工业健康发展的指导意见》其中第三项\"主要任务\"中明确指出，在\"十三五\"期间要大力支持发展功能性蛋白和生物活性肽等健康食品。这是国家首次在政策层面明确提出要将发展活性肽产业放在优先发展位置，对于肽行业来说释放了一个非常积极的信号。\n" +
							"\n" +
							"富迪肽公司响应国家号召，推出了富迪低聚肽固体饮料产品。以优质大豆为原料提取。为什么要用大豆作为原料提取，因为大豆的肽氨基酸组成成分最为齐全。大家可以查看一下大豆的氨基酸组成成分表。");
					break;
				case 3:
					v.setText("富迪大豆低聚肽的产品优势:" +
							"\n" +
							 "1、产品未经任何化学及物理性的脱色、脱味处理，保留了大豆蛋白质原有的氨基酸平衡比例及营养价值。\n" +
							"\n" +
							"2、富含功能性小分子肽片段，每种产品均采用多级定向酶解工艺技术，具备各自新的、独特的生物学功效，每项生物学功效都通过大量的动物实验和临床试验验证。\n" +
							"\n" +
							"3、产品可完全溶于水，溶液澄清透明，可被人体直接、快速、完全吸收，产品游离氨基酸含量低，蛋白肽含量大于97%。\n" +
							"\n" +
							"4、根据生物学功效将产品细分化，不同的产品能满足不同人群及不同产品领域的需求，低至敏性，大量使用不会引起过敏反应。风味、口感纯正，无任何臭味、异味。 "
					       );
					break;
				case 4:
					v.setText("富迪大豆低聚肽的理化特性"+
							"\n" +
							"分子量小、易吸收\n" +
							"分子量集中在180-500道尔顿之间，体外试验证实，大豆低聚肽经胃蛋白酶处理后，95%以上未被消化；经胰蛋白酶处理后，约90%未被消化。这说明大豆低聚肽不再被消化，大部分将以低聚肽的形式被直接吸收。不是所有肽都叫低聚肽,分子量在500-2000道尔顿称为小分子活性肽,分子量在180-500道尔顿才称为低聚肽。\n" +
							"\n" +
							"优良的水溶性\n" +
							"与整蛋白相比，肽溶解性好，50％高浓度仍有很好的流动性。溶解度达到99.9%，流动性良好；且溶解度对温度不敏感，在冷水中即可溶解。\n" +
							"\n" +
							"酸稳定性\n" +
							"蛋白质通常具有沉淀点，一般当pH值在4.5左右，溶液中的蛋白质即会沉淀。大豆低聚肽没有等电点，在pH4.5的酸性条件也不会沉淀。");
					break;
				case 5:
					v.setText("安全性"+
							"\n" +
							"大豆内存在很多的抗营养物质，像胰蛋白酶抑制剂、红细胞凝集素、抗维生素物质等，会对人体产生危害。大豆低聚肽中不含抗营养成分。研究报道，大豆低聚肽具有极低的致敏性。因此，食用更安全。\n" +
							"\n" +
							"富迪大豆低聚肽的生理功能\n" +
							"（1）改善小肠组织结构和吸收功能\n" +
							"\n" +
							"试验证实，大豆低聚肽可以增加小肠粘膜绒毛高度，加深隐窝深度，增加肠粘膜的吸收面积，促进小肠腺体发达，增加氨基肽酶的活性。因此能够促进小肠吸收功能的改善。\n" +
							"\n" +
							"（2）增强机体的免疫力\n" +
							"\n" +
							"大豆低聚肽能够改善机体的细胞免疫功能。它可以促进T淋巴细胞增殖，增强巨噬细胞的功能，增强NK细胞的活性。有研究报道，大豆低聚肽还可以促进肿瘤坏死因子的产生。\n" +
							"\n" +
							"（3）降血压\n" +
							"\n" +
							"血管紧张素I在血管紧张素转换酶的作用下转变为血管紧张素II。这种转换产物，能使末梢血管收缩作用增强，从使血压升高。大豆低聚肽可以抑制血管紧张素转换酶(ACE)的活性，因此能够起到降低压的效果。但大豆低聚肽对正常血压者几乎没有影响。\n" +
							"\n" +
							"（4）血脂调节作用   \n" +
							"\n" +
							"大豆低聚肽能够通过降低血清总胆固醇、降低甘油三酯、降低低密度脂蛋白胆固醇，有效调节血脂水平。\n" +
							"\n" +
							"（5）促进脂肪代谢\n" +
							"\n" +
							"大豆低聚肽可增加褐色脂肪里面线粒体的活性，促进脂肪代谢；还能增加去甲肾上腺素的转化率，减少对脂肪酶的抑制，从而促进脂肪代谢。\n" +
							"\n" +
							"（6）抗氧化\n" +
							"\n" +
							"通过大豆低聚肽可以提高超氧化物歧化酶、谷胱甘肽过氧化物酶的活性，抑制脂质过氧化，清除自由基，有利于减少组织氧化，保护机体。\n" +
							"\n" +
							"（7）抗运动疲劳\n" +
							"\n" +
							"大豆低聚肽能够及时修复运动过程中损伤的骨骼肌细胞的修复，维持骨骼肌细胞结构和功能的完整性。同时，能够增加睾酮的分泌，促进蛋白质合成。人体试验证实，大豆低聚肽能够降低中长跑运动员运动后PRE的等级，有助于减轻运动后疲劳。\n" +
							"\n" +
							"肽国家标准\n" +
							"国家关于大豆肽产品的等级划分标准是：肽含量高于80%的为一级产品。\n" +
							"\n" +
							"富迪大豆低聚肽的成分及重金属检测报告\n" +
							"富迪肽大豆低聚肽产品检测报告结果显示，肽含量高达87%，属于国家一级产品标准。重金属检测报告表明，富迪肽产品各项指标远远优于国家规定限制，不含任何重金属。");
					break;
				case 6:
					v.setText("公司介绍"+
							"\n" +
					        "富迪肽公司，是一家集研发、生产、销售高科技产品于一体的跨国企业，目前公司拥有员工400多人，包括多位具有丰富跨国企业管理经验的高级管理人员；公司全球合作销售伙伴达50万人，产品销往全球各地，并在美国、俄罗斯、韩国、马来西亚、新加坡、印尼、菲律宾、泰国、越南、香港、台湾等国家和地区设立了分支机构。身为中国前十家获得合法牌照的公司自2007年1月29日启动市场起。2007年在国内业绩突破21个亿，至2009年三年合计业绩突破100个亿。在全球70个国家开展业务，完成从黑马到神话的蜕变。如今投入肽产业，必然是继续秉持做到最好最强为理念，将传奇推上更高的辉煌。"+
							"\n" +
							"公司董事长：陈怀德，广东省第十二届人大代表，中国扶贫开发协会副会长；中国经济贸易促进会副会长；广州市侨商会副会长；2007年至今已累计向社会各界捐赠款物超过3亿元人民币，累计8次获得“中国十大慈善家”荣誉。"+
							"\n" +
							"CEOCEO"+
							"对于富迪肽精彩项目，陈董事长给予高度的重视与对未来坚定的信心，他表示，目前这个行业长期由外国人垄断，偌大中国凭什么长期由外资垄断，在中国的土壤上，一定是中国的企业说了算。“我伸出的手不一定是最有力的，但一定是最富有有责任的，今后不管发生什么，我一定站在你们身边，生死与共”。伟大的梦想需要配以大胆的行动，富迪提供了一项可靠的机会与坚实的臂膀，并会帮助富迪肽家人扫除前进路上的一切障碍。”");
					break;
			}
			final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources()
					.getDisplayMetrics());
			v.setPadding(padding, padding, padding, padding);
			v.setGravity(Gravity.LEFT);
			container.addView(v, 0);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object view) {
			container.removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View v, Object o) {
			return v == ((View) o);
		}

	}




}
